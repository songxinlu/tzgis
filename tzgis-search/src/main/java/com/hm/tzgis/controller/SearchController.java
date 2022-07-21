package com.hm.tzgis.controller;

import com.hm.tzgis.entity.Devicetype;
import com.hm.tzgis.entity.FeatureEntity;
import com.hm.tzgis.mapper.DataMapper;
import com.hm.tzgis.service.SearchService;
import com.hm.tzgis.utils.AjaxType;
import com.hm.tzgis.utils.SblxUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hm.tzgis.entity.ZtTypeName;
import com.hm.tzgis.dao.MangoBaseDao;
import com.hm.tzgis.entity.Model;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shike
 * @date 2022/4/2 14:14
 */

@RestController
public class SearchController {

    @Autowired
    private MangoBaseDao mongoBaseDao;
    @Autowired
    private DataMapper dataMapper;
    @Autowired
    private SearchService searchService;


    @PostMapping("getTestData")
    public void getTestData(){

        List<ZtTypeName>  listDeviceType = mongoBaseDao.findAll(ZtTypeName.class, "devicetype");
        for (ZtTypeName ztTypeName : listDeviceType) {
            Query query = new Query().addCriteria(Criteria.where("properties.line").in("39633","10929"));
            List<Model> list = mongoBaseDao.find(query, Model.class, ztTypeName.getDevicetype());
            mongoBaseDao.insertAll(list, ztTypeName.getDevicetype()+"_");
        }
    }

    @PostMapping("shapeUpdate")
    public void shapeUpdate(){
        double lnt = 13.49612493268917;  //经度差
        double lat = -4.3758970633173675;  //纬度差


//        List<ZtTypeName>  listDeviceType = mongoBaseDao.findAll(ZtTypeName.class, "devicetype");
//        for (ZtTypeName ztTypeName : listDeviceType) {
//            List<Model> list = mongoBaseDao.findAll(Model.class, ztTypeName.getDevicetype());
            List<Model> list = mongoBaseDao.findAll(Model.class,"713");

            for (Model model : list) {
                if(model.getGeometry() != null){

                    if(model.getGeometry().getType().equals("Point")){
                        JSONArray jArray = model.getGeometry().getCoordinates();
                        double x = Double.parseDouble(jArray.get(0).toString());
                        double y = Double.parseDouble(jArray.get(1).toString());
                        jArray.set(0, x+lnt);
                        jArray.set(1, y+lat);
                    }

                    if(model.getGeometry().getType().equals("LineString")){
                        JSONArray jArray = model.getGeometry().getCoordinates();
                        for (int i = 0; i < jArray.size(); i++) {
                            JSONArray lineString = jArray.getJSONArray(i);
                            double x = Double.parseDouble(lineString.get(0).toString());
                            double y = Double.parseDouble(lineString.get(1).toString());
                            lineString.set(0, x+lnt);
                            lineString.set(1, y+lat);
                        }
                    }
                    if(model.getGeometry().getType().equals("MultilineString")){
                        JSONArray jArray = model.getGeometry().getCoordinates();
                        for (int i = 0; i < jArray.size(); i++) {
                            JSONArray lineString = jArray.getJSONArray(i);
                            for (int j = 0; j < lineString.size(); j++) {
                                JSONArray multilineString = lineString.getJSONArray(j);
                                double x = Double.parseDouble(multilineString.get(0).toString());
                                double y = Double.parseDouble(multilineString.get(1).toString());
                                multilineString.set(0, x+lnt);
                                multilineString.set(1, y+lat);
                            }
                        }
                    }
                }
                    if(model.getJo().getString("shape").contains("POINT")){
                        String shape = model.getJo().getString("shape").replace("POINT (","").replace(")","");
                        StringBuilder endShape = new StringBuilder("POINT (");
                        String[] shapes = shape.split(", ");
                        for (String str : shapes) {
                            String[] xy = str.split(" ");
                            double x = Double.parseDouble(xy[0]);
                            double y = Double.parseDouble(xy[1]);
                            endShape.append((x+lnt)+" "+(y+lat)+", ");
                        }
                        String endshape1 = endShape.substring(0,endShape.length()-2)+")";
                        model.getJo().put("shape", endshape1);
                    }
                    if(model.getJo().getString("shape").contains("POLYGON")){
                        String shape = model.getJo().getString("shape").replace("POLYGON ((","").replace("))","");
                        StringBuilder endShape = new StringBuilder("POLYGON ((");
                        String[] shapes = shape.split(", ");
                        for (String str : shapes) {
                            String[] xy = str.split(" ");
                            double x = Double.parseDouble(xy[0]);
                            double y = Double.parseDouble(xy[1]);
                            endShape.append((x+lnt)+" "+(y+lat)+", ");
                        }
                        String endshape1 = endShape.substring(0,endShape.length()-2)+"))";
                        model.getJo().put("shape", endshape1);
                    }
                    if(model.getJo().getString("shape").contains("LINESTRING")){
                        String shape = model.getJo().getString("shape").replace("LINESTRING (","").replace(")","");
                        StringBuilder endShape = new StringBuilder("LINESTRING (");
                        String[] shapes = shape.split(", ");
                        for (String str : shapes) {
                            String[] xy = str.split(" ");
                            double x = Double.parseDouble(xy[0]);
                            double y = Double.parseDouble(xy[1]);
                            endShape.append((x+lnt)+" "+(y+lat)+", ");
                        }
                        String endshape1 = endShape.substring(0,endShape.length()-2)+")";
                        model.getJo().put("shape", endshape1);
                    }

            }

            mongoBaseDao.dropCollection("713");
            mongoBaseDao.insertAll(list, "713");
//            mongoBaseDao.dropCollection(ztTypeName.getDevicetype());
//            mongoBaseDao.insertAll(list, ztTypeName.getDevicetype());

        }
//    }



    //设备基础数据加入所属网格
    private List<JSONObject> griddingDevice(List<Map> devices, String gridding_id, String version_id,String devicetype){
        List<JSONObject> lists = new ArrayList<>();

        for (Map device : devices) {
            JSONObject entity = new JSONObject();
            JSONObject properties = new JSONObject();

            for (Object key : device.keySet()) {
                properties.put(key.toString().toLowerCase(), device.get(key));
            }
            properties.put("griddingid", gridding_id);
            properties.put("versionid", version_id);
            properties.put("objtype", devicetype);
            properties.put("devicetype", devicetype);

            entity.put("id", properties.getString("id"));
            entity.put("properties", properties);

            lists.add(entity);
        }

        return lists;
    }

//    @RequestMapping("loadData")
//    public AjaxType loadData(@RequestParam("gridding_id")String gridding_id, @RequestParam("version_id")String version_id){
//
//        JSONObject obj = new JSONObject();
//
//        List<Devicetype> devicetypeList = mongoBaseDao.findAll(Devicetype.class, "devicetype");
//
//        Query query = new Query().addCriteria(Criteria.where("GRIDDING_ID").is(gridding_id));
//        List<JSONObject> lineList = mongoBaseDao.find(query, JSONObject.class, "grdding_line");
////        String lineids = lineList.stream().map(JSONObject->JSONObject.getString("LINE_ID")).collect(Collectors.joining(","));
//
//        Map<String, List<Devicetype>> devicetypeMap = devicetypeList.stream().collect(Collectors.groupingBy(Devicetype::getType));
//
//        //配网设备
//        List<Devicetype> lineDevices = devicetypeMap.get("3");
//        for (Devicetype devicetype : lineDevices) {
//            for (JSONObject jsonObject : lineList) {
//                String lineId = jsonObject.getString("LINE_ID");
//
//                List<Map> maps;
//                if(devicetype.getDevicetype().equals("700")){
//                    String sql = devicetype.getModelName()+" and a.id = "+lineId;
//                    maps = dataMapper.queryData(sql);
//                }else{
//                    String sql = devicetype.getModelName()+" and a.feeder_id = "+lineId;
//                    maps = dataMapper.queryData(sql);
//                }
//                if(maps.isEmpty()){
//                    continue;
//                }
//                griddingDevice(maps, gridding_id, version_id, devicetype.getDevicetype());
//                mongoBaseDao.insertAll(maps, devicetype.getDevicetype());
//            }
//        }
//
//        //站房设备
//        List<Devicetype> stationDevices = devicetypeMap.get("1");
//        //站内设备
//        List<Devicetype> instationDevices = devicetypeMap.get("2");
//        for (Devicetype stationDevice : stationDevices) {
//            for (JSONObject jsonObject : lineList) {
//
//                List<Map> maps;
//                if(stationDevice.getDevicetype().equals("401")){
//                    String substationId = jsonObject.getString("SUBSTATION_ID");
//                    String sql = stationDevice.getModelName()+" and a.id = "+substationId;
//                    maps = dataMapper.queryData(sql);
//                }else{
//                    String lineId = jsonObject.getString("LINE_ID");
//                    String sql = stationDevice.getModelName()+" and a.feeder_id = "+lineId;
//                    maps = dataMapper.queryData(sql);
//                }
//                if(maps.isEmpty()){
//                    continue;
//                }
//                griddingDevice(maps, gridding_id, version_id, stationDevice.getDevicetype());
//                mongoBaseDao.insertAll(maps, stationDevice.getDevicetype());
//
//                String stationIds = maps.stream().map(Map->Map.get("ID").toString()).collect(Collectors.joining(","));
//                for (Devicetype instationDevice : instationDevices) {
//                    String sql = instationDevice.getModelName()+" and a.combined_id in("+stationIds+")";
//                    maps = dataMapper.queryData(sql);
//                    griddingDevice(maps, gridding_id, version_id, instationDevice.getDevicetype());
//                    mongoBaseDao.insertAll(maps, instationDevice.getDevicetype());
//                }
//
//            }
//        }
//
//        return new AjaxType(200, true, "success", obj);
//    }


    /**
     * 主站数据拉取
     * @param gridding_id
     * @param version_id
     * @return
     */
    @RequestMapping("loadData")
    public AjaxType loadData(@RequestParam("gridding_id")String gridding_id, @RequestParam("version_id")String version_id){

        JSONObject obj = new JSONObject();

        List<Devicetype> devicetypeList = mongoBaseDao.findAll(Devicetype.class, "devicetype");

        Query query = new Query().addCriteria(Criteria.where("GRIDDING_ID").is(gridding_id));
        List<JSONObject> lineList = mongoBaseDao.find(query, JSONObject.class, "grdding_line");

        Map<String, List<Devicetype>> devicetypeMap = devicetypeList.stream().collect(Collectors.groupingBy(Devicetype::getType));
        //变电设备
        List<Devicetype> bdDevices = devicetypeMap.get("3");

        //变电站
        List<Devicetype> substationDevice = devicetypeMap.get("2");

        //配网设备
        List<Devicetype> pwDevices = devicetypeMap.get("1");

        Set substationIds = new HashSet<>();
        for (Devicetype devicetype : pwDevices) {
            for (JSONObject jsonObject : lineList) {
                String lineId = jsonObject.getString("LINE_ID");

                List<Map> maps;
                if(devicetype.getDevicetype().equals("700")){
                    String sql = devicetype.getModelName()+" and a.id = "+lineId;
                    maps = dataMapper.queryData(sql);
                    for (Map map : maps) {
                        Object substationId = map.get("PID");
                        substationIds.add(substationId);
                    }
                }else{
                    String sql = devicetype.getModelName()+" and a.feeder_id = "+lineId;
                    maps = dataMapper.queryData(sql);
                }
                if(maps.isEmpty()){
                    continue;
                }
                mongoBaseDao.insertAll(griddingDevice(maps, gridding_id, version_id, devicetype.getDevicetype()), devicetype.getDevicetype());
            }
        }

        for (Object substationId : substationIds) {
            String sql = substationDevice.get(0).getModelName()+" and a.id = "+substationId;
            List<Map> maps = dataMapper.queryData(sql);
            if(maps.isEmpty()){
                continue;
            }
            mongoBaseDao.insertAll(griddingDevice(maps, gridding_id, version_id, substationDevice.get(0).getDevicetype()), substationDevice.get(0).getDevicetype());
            for (Devicetype bdDevice : bdDevices) {
                sql = bdDevice.getModelName()+" and a.combined_id = "+substationId +" and a.dydj = 112871465677750278";
                maps = dataMapper.queryData(sql);
                if(maps.isEmpty()){
                    continue;
                }
                mongoBaseDao.insertAll(griddingDevice(maps, gridding_id, version_id, bdDevice.getDevicetype()), bdDevice.getDevicetype());
            }
        }
        return new AjaxType(200, true, "success", obj);
    }


    @RequestMapping("testFile")
    public void testFile() throws Exception{

        FileInputStream is = new FileInputStream("C:\\Users\\86151\\Desktop\\泰州项目设备类型和设备属性.xlsx");


        XSSFWorkbook xSSFWorkbook = null;
        XSSFSheet xSSFSheet = null;
        xSSFWorkbook = new XSSFWorkbook(is);
        for (int i = 1; i < 5; i++) {
            try {
                //获取第一个sheet
                xSSFSheet = xSSFWorkbook.getSheetAt(i);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            if (xSSFSheet != null) {
                //构建每行的数据内容
                //输出行数据
                int rownum = 0;     //数据起始行数
                for (int j = 1; j <= 400; j++) {
                    XSSFRow row = xSSFSheet.getRow(rownum++);
                    if(row == null){
                        continue;
                    }
                    XSSFCell cell = row.getCell(1);
                    if(cell == null){
                        continue;
                    }
                    String str = cell.getStringCellValue();
                    if(str.equals("全局标志") ||
                            str.equals("外部系统ID") ||
                            str.equals("投运日期") ||
                            str.equals("运行编号") ||
                            str.equals("使用性质") ||
                            str.equals("创建时间") ||
                            str.equals("维护班组") ||
                            str.equals("修改时间")
                    ){
                        xSSFSheet.removeRow(row);
                    }
                }
            }
        }

        FileOutputStream os = new FileOutputStream("C:\\Users\\86151\\Desktop\\111.xlsx");
        xSSFWorkbook.write(os);
    }


//    @RequestMapping("testSql")
//    public void testSql(){
//
//        List<Map> objList = wtkMapper.queryObjmap();
//
//        JSONObject json;
//        for (Map map : objList) {
//
//            String field = map.get("SRC_FIELD").toString();
//            field = field.replace(",a.feeder_id", ",a.feeder_id ssdkx").
//            replace(",a.feeder_id_str", ",a.feeder_id_str ssdkx").
//            replace(",a.rdf_id", ",a.rdf_id prsid").
//            replace(",a.device_asset_id", ",a.device_asset_id assetid").
//            replace(",a.record_app3", "").
//            replace(",a.fid", "").
//            replace(",a.combined_id", ",a.combined_id pid").
//            replace(",a.run_state", "").
//            replace(",a.subtype", "").
//            replace(",1 subtype", "").
//            replace(",a.workspace", "").
//            replace(",a.area_id", "").
//            replace(",a.ctrl_mode", "").
//            replace(",a.unit_id", "").
//            replace(",b.name model", "").
//            replace(",a.nd_no_red fnod_red", "").
//            replace(",a.qsdz", ",a.qsdz pid").
//            replace(",a.is_3y", "");
//
//
//            json = new JSONObject();
//            json.put("devicetype", map.get("OBJ_TYPE"));
//            json.put("modelName", "select " + field + " from " + map.get("SRC_TABLE") + " where " + map.get("SRC_FILTER"));
//            System.out.println(json);
//        }
//
//    }

}
