package com.hm.tzgis.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hm.tzgis.dao.MangoBaseDao;
import com.hm.tzgis.entity.Devicetype;
import com.hm.tzgis.entity.MongoMapEntity;
import com.hm.tzgis.entity.MongoObjectEntity;
import com.hm.tzgis.service.SearchService;
import com.hm.tzgis.util.MongDBUtils;
import com.hm.tzgis.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shike
 * @date 2022/4/28 15:36
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Value("${data.filePath}")
    private String path;

    @Autowired
    private MangoBaseDao mongoBaseDao;


    @Override
    public String getGeojson(String gridding) {
        long taskid = System.currentTimeMillis();

        List<Devicetype> devicetypeList = mongoBaseDao.findAll(Devicetype.class, "devicetype");

        JSONObject json = new JSONObject();
        json.put("diagramType","300500");
        try {
            List<Map> devices = new ArrayList<>();

            List<Map> linelist = new ArrayList<>();

            Set<String> devicetypeSet = new HashSet<>();
            for (Devicetype devicetype : devicetypeList) {
                if(devicetypeSet.contains(devicetype.getDevicetype())){
                    continue;
                }
                devicetypeSet.add(devicetype.getDevicetype());

                Query query = new Query();
                query.fields().exclude("_id");
                query.addCriteria(Criteria.where("GRIDDINGID").is(gridding));
                List<Map> list = mongoBaseDao.find(query, Map.class, devicetype.getDevicetype());
                devices.addAll(list);

                if(devicetype.getDevicetype().equals("700")){
                    for (Map map : list) {
                        Map lineMap = new HashMap();
                        lineMap.put("id",map.get("ID"));
                        lineMap.put("name",map.get("NAME"));
                        lineMap.put("psrType","700");
                        lineMap.put("voltage","22");
                        lineMap.put("substation",map.get("PID"));
                        linelist.add(lineMap);
                    }

                }
            }

            if (devices.size() > 0) {
                for(Map map : devices) {
                    List<String> connectionList = new ArrayList<>();
                    if (map.get("FNODE") != null && !map.get("FNODE").equals("")) {
                        connectionList.add(map.get("FNODE").toString());
                    }
                    if (map.get("TNODE") != null && !map.get("TNODE").equals("")) {
                        connectionList.add(map.get("TNODE").toString());
                    }
                    map.put("connection", connectionList);
                }
            }
            json.put("linelist",linelist);
            json.put("devices", devices);

            String msgPath = taskid+".json";
            String topoPath = taskid+".json";

            JSONObject msgJson = new JSONObject();
            msgJson.put("type","2018");
            msgJson.put("objType","1638");
            msgJson.put("taskId",String.valueOf(taskid));

            FileUtils.uploadFile(path+"/msg/", msgPath, msgJson.toJSONString());
            FileUtils.uploadFile(path+"/topo/", topoPath, json.toJSONString());

            //C服务启动命令
            String cProc = path+"/bin/HMMidPlfAppMR32 -c " +
                    "-m"+path+"/msg/"+msgPath+" -t"+path+"/topo/"+topoPath;

            System.out.println(cProc);
            Process ps = Runtime.getRuntime().exec(cProc);

            new DealProcessSream(ps.getInputStream()).start();
            new DealProcessSream(ps.getErrorStream()).start();

            int code = ps.waitFor();

            if(code == 1){
                StringBuilder sb = new StringBuilder();

                FileReader fileReader = null;
                BufferedReader br = null;
                try {
                    File file = new File(path+"/ret/topo/"+taskid+"web.json");
                    if(file.exists()){
                        fileReader = new FileReader(path+"/ret/topo/"+taskid+"web.json");
                        br = new BufferedReader(fileReader);
                        String temp;
                        while((temp = br.readLine() ) != null){
                            sb.append(temp);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    try {
                        if(fileReader != null){
                            fileReader.close();
                        }
                        if(br != null){
                            br.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return insertAllGeo(sb.toString(), gridding);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String insertAllGeo(String geoJson, String gridding) {
        JSONObject json = JSONObject.parseObject(geoJson).getJSONObject("geoJson");
        String zttobjes = json.get("zttobjs").toString();
        //先转换对象
        List<MongoObjectEntity> arrayList = JSONArray.parseArray(zttobjes, MongoObjectEntity.class);
        if(arrayList == null || arrayList.size() <=0) {
            return "";
        }
        //获取设备的类型，根据类型查询表
        StringBuilder objTypes = new StringBuilder();
        Map<String,List<MongoObjectEntity>> map = new HashMap<>();
        for(MongoObjectEntity obj : arrayList) {
            String objType = obj.getProperties().get("objtype");
            if(map.containsKey(objType)) {
                map.get(objType).add(obj);
            }else {
                objTypes.append(objType).append(",");
                map.put(objType, new ArrayList<>());
                map.get(objType).add(obj);
            }
        }
        //添加图纸主表
        MongoMapEntity mongoMapEntity = new MongoMapEntity();
        mongoMapEntity.setWorldid(json.getString("worldid"));
        mongoMapEntity.setViewbox(json.getString("viewbox"));
        mongoMapEntity.setObjNum(json.getString("obj_num"));
        mongoMapEntity.setObjTypes(objTypes.toString());
        mongoMapEntity.setRelationlines(json.getString("relationlines"));
        mongoMapEntity.setBaseobjid(gridding);
        mongoMapEntity.setBaseobjtype("550");
        mongoBaseDao.insert(mongoMapEntity, MongDBUtils.MONGODB_ZTT_WORLDID_OBJTYPE);
        //从redis中获取数据设备类型对应的表的配置信息
        for(String key : map.keySet()) {
            mongoBaseDao.insert(map.get(key), MongDBUtils.MONGODB_ZTT + key);
        }
        return mongoMapEntity.getWorldid();
    }

}
