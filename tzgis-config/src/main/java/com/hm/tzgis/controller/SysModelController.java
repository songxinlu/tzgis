package com.hm.tzgis.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hm.tzgis.dao.MangoBaseDao;
import com.hm.tzgis.entity.*;
import com.hm.tzgis.service.SearchService;
import com.hm.tzgis.service.SysModelService;
import com.hm.tzgis.util.CommonResult;
import com.hm.tzgis.util.DateUtil;
import com.hm.tzgis.util.StringUtils;
import com.hm.tzgis.utils.SblxUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class SysModelController {

    @Autowired
    private SysModelService sysModelService;

    @Autowired
    private MangoBaseDao mongoBaseDao;

    @Autowired
    private SearchService searchService;


    /**
     * @param sysModel
     * @return
     */
    @RequestMapping("querySysModelList")
    public CommonResult querySysModelList(@RequestBody SysModel sysModel) {
        List<SysModel> dataList = sysModelService.querySysModel(sysModel);
        return CommonResult.success(dataList);
    }

    /**
     * 查询所有
     *
     * @param sysModelSymbol
     * @return
     */
    @RequestMapping("querySysModelSymbolList")
    public CommonResult querySysModelSymbolList(@RequestBody SysModelSymbol sysModelSymbol) {
        List<SysModelSymbol> dataList = sysModelService.querySysModelSymbol(sysModelSymbol);
        return CommonResult.success(dataList);
    }

    /**
     * 查询所有
     *
     * @param sysSymbolJson
     * @return
     */
    @RequestMapping("querySysSymbolJsonList")
    public CommonResult querySysSymbolJsonList(@RequestBody SysSymbolJson sysSymbolJson) {
        List<SysSymbolJson> dataList = sysModelService.querySysSymbolJson(sysSymbolJson);
        return CommonResult.success(dataList);
    }

    /**
     * 查询所有
     *
     * @param sysDispConfig
     * @return
     */
    @RequestMapping("querySysDispConfigList")
    public CommonResult querySysDispConfigList(@RequestBody SysDispConfig sysDispConfig) {
        List<SysDispConfig> dataList = sysModelService.querySysDispConfig(sysDispConfig);
        return CommonResult.success(dataList);
    }

    /**
     * 获取配置属性
     *
     * @param record
     * @return
     */
    @PostMapping("queryBySysSketchmapPara")
    public CommonResult queryBySysSketchmapPara(@RequestBody SysSketchmapPara record) {
        record.setAreaid(0L);
        if (!StringUtils.isEmpty(record.getWorldId())) {
            TxMapVersion txMapVersion = new TxMapVersion();
            txMapVersion.setWorldId(record.getWorldId());
            TxMapVersion t = sysModelService.queryByWorldid(txMapVersion);
            if (t != null) {
                record.setUniverse(Short.parseShort(t.getMapTypeId()));
            }
        }
        record.setEnabled((short) 0);
        List<SysSketchmapPara> list = sysModelService.querySysSketchmapPara(record);
        return CommonResult.success(list);
    }

    /**
     * 列表分页
     *
     * @return
     */
    @PostMapping("querySysUniverseConfig")
    public CommonResult querySysUniverseConfig(@RequestBody SysUniverseConfig sysUniverseConfig) {
        List<SysUniverseConfig> list = sysModelService.querySysUniverseConfig(sysUniverseConfig);
        return CommonResult.success(list);
    }

    /**
     * 查询所有
     *
     * @return
     */
    @RequestMapping("querySysEnvironmentBySection")
    public CommonResult querySysEnvironmentBySection(@RequestBody LoginUserBean bean) {

        SysEnvironment sysEnvironment = sysModelService.querySysEnvironmentBySection(bean.getSsywdwId(), "UnUnifiedSwitchTypeList");
        return CommonResult.success(sysEnvironment);
    }

    /**
     * 查询所有
     *
     * @return
     */
    @RequestMapping("getGisSysColorByDispType")
    public CommonResult getGisSysColorByDispType(@RequestBody SysDispConfig sysDispConfig) {
        List<GisSysColorBean> dataList = sysModelService.getGisSysColorByDispType((long) sysDispConfig.getUniverse());
        return CommonResult.success(dataList);
    }

    /**
     * 获取树状排列的ButtonConfig：  专题图编辑 模块的菜单按钮配置数据
     *
     * @return 树状排列的ButtonConfig
     */
    @RequestMapping("/queryTreeTxButtonConfig")
    public CommonResult queryTreeTxButtonConfig() {
        return CommonResult.success(sysModelService.queryTreeTxButtonConfig());
    }


    /**
     * @param gridding_id
     * @param status      status 0：现状；1:规划推演；2：储备推演；3：计划推演；4：历史
     * @Description 根据网格ID, 状态获取地理图设备数据
     * @Method getDeviceData
     * @Author songxl
     * @Return com.hm.tzgis.entity.AjaxType
     */
    @RequestMapping("getDeviceData")
    @ResponseBody
    public AjaxType getDeviceData(@RequestParam("gridding_id") String gridding_id,
                                  @RequestParam("status") int status) {

        JSONObject obj = new JSONObject();
        List<FeatureEntity> lists = new LinkedList<>();
        //推演数据
        List<FeatureEntity> editRecords = new ArrayList();
        Map<String, List<FeatureEntity>> editMap = new HashMap<>();
        //收集删除、修改状态的id
        List<String> upOrdelDeviceIds = new ArrayList<>();
        //properties.objoprtflag :0基础 1新增 2修改 3删除
        //properties.id :设备id
        //properties.objtype : 对象类型
        //properties.gridding_id :网格
        if (status > 0 && status <= 3) {
            Query query = new Query().addCriteria(Criteria.where("properties.status").is(status)
                    .and("properties.gridding_id").is(gridding_id));
            editRecords = mongoBaseDao.find(query, FeatureEntity.class, "edit_record");
            //数据处理
            dataProcessing(editMap, editRecords, upOrdelDeviceIds);
        }

        List<Devicetype> devicetypeList = mongoBaseDao.findAll(Devicetype.class, "devicetype");

        Set<String> devicetypeSet = new HashSet<>();
        for (Devicetype devicetype : devicetypeList) {
            if (devicetypeSet.contains(devicetype.getDevicetype())) {
                continue;
            }
            devicetypeSet.add(devicetype.getDevicetype());
            Query query = new Query().addCriteria(Criteria.where("properties.objoprtflag").ne("3"))//操作类型不是3（删除）
                    .addCriteria(Criteria.where("properties.gridding_id").is(gridding_id));
            if (status > 0 && status <= 3) {
                //去除修改删除的现在数据
                query.addCriteria(Criteria.where("id").nin(upOrdelDeviceIds));
                //将新增和修改的推演数据添加到 返回结果list
                if (editMap.get("1") != null) {
                    lists.addAll(editMap.get("1"));
                }
                if (editMap.get("2") != null) {
                    lists.addAll(editMap.get("2"));
                }
            }
            if (status == 4) {
                //历史数据查询流程
            }
            List<FeatureEntity> list = mongoBaseDao.find(query, FeatureEntity.class, devicetype.getDevicetype());
            lists.addAll(list);
        }

        obj.put("geoJson", lists);
        return new AjaxType("200", true, "success", obj);
    }

    /**
     * @param editMap          通过操作类型将推演数据分类
     * @param editRecords      某一网格某一状态下所有的推演数据
     * @param upOrdelDeviceIds 更新、删除操作推演数据的id
     * @Description数据处理
     * @Method dataProcessing
     * @Author songxl
     * @Return void
     */
    private void dataProcessing(Map<String, List<FeatureEntity>> editMap, List<FeatureEntity> editRecords, List<String> upOrdelDeviceIds) {

        if (editRecords == null || editRecords.size() == 0) {
            return;
        }
        editRecords.stream().forEach(featureEntity -> {
            if (!editMap.keySet().contains(featureEntity.getProperties().getString("objoprtflag"))) {
                List<FeatureEntity> temp = new ArrayList<>();
                editMap.put(featureEntity.getProperties().getString("objoprtflag"), temp);
            }
            editMap.get(featureEntity.getProperties().getString("objoprtflag")).add(featureEntity);
            //收集删除或修改状态的推演数据id
            if ("2".equals(featureEntity.getProperties().getString("objoprtflag")) ||
                    "3".equals(featureEntity.getProperties().getString("objoprtflag"))) {
                upOrdelDeviceIds.add(featureEntity.getId());
            }

        });
    }

    //查询所有设备和网格行政区数据
    //临时用
    @RequestMapping("getDataByGridding")
    @ResponseBody
    public AjaxType getDataByGridding(@RequestParam("gridding_id") String gridding_id) {

        JSONObject obj = new JSONObject();
        List<FeatureEntity> lists = new LinkedList<>();

        List<Devicetype> devicetypeList = mongoBaseDao.findAll(Devicetype.class, "devicetype");

        Set<String> devicetypeSet = new HashSet<>();
        for (Devicetype devicetype : devicetypeList) {
            if (devicetypeSet.contains(devicetype.getDevicetype())) {
                continue;
            }
            devicetypeSet.add(devicetype.getDevicetype());
            Query query = new Query().addCriteria(Criteria.where("properties.objoprtflag").ne("3"));
            List<FeatureEntity> list = mongoBaseDao.find(query, FeatureEntity.class, devicetype.getDevicetype());
            lists.addAll(list);
        }

        //网格
        List<FeatureEntity> grddingList = mongoBaseDao.findAll(FeatureEntity.class, SblxUtil.GRDDING);
        lists.addAll(grddingList);
        //行政区
        List<FeatureEntity> districtList = mongoBaseDao.findAll(FeatureEntity.class, SblxUtil.DISTRICT);
        lists.addAll(districtList);

        obj.put("geoJson", lists);
        return new AjaxType("200", true, "success", obj);
    }


    //获取网格和行政区数据
    @RequestMapping("getGriddingAndDistrict")
    @ResponseBody
    public AjaxType getGriddingAndDistrict() {

        JSONObject obj = new JSONObject();
        List<FeatureEntity> lists = new LinkedList<>();
        //网格
        List<FeatureEntity> grddingList = mongoBaseDao.findAll(FeatureEntity.class, SblxUtil.GRDDING);
        lists.addAll(grddingList);
        //行政区
        List<FeatureEntity> districtList = mongoBaseDao.findAll(FeatureEntity.class, SblxUtil.DISTRICT);
        lists.addAll(districtList);

        obj.put("geoJson", lists);
        return new AjaxType("200", true, "success", obj);
    }

    /**
     * 专题图成图接口
     *
     * @param json
     * @return
     */
    @RequestMapping("getGeojson")
    public AjaxType getGeojson(@RequestBody JSONObject json) {
        return AjaxType.success(searchService.getGeojson(json.getString("gridding_id"), json.getJSONArray("project_id").toJavaList(String.class)));
    }

    /**
     * @param
     * @Description 通过网格初始化推演数据版本
     * @Method initDeductionVersion
     * @Author songxl
     * @Return com.hm.tzgis.entity.AjaxType
     */
    @PostMapping("initVersionByGrid")
    @Transactional
    public AjaxType initVersionByGrid() {
        List<Version> inserts = new ArrayList<>();
        //获取所有网格
        List<FeatureEntity> grddingList = mongoBaseDao.findAll(FeatureEntity.class, SblxUtil.GRDDING);
        //获取网格【status:[1,2,3]】所有版本
        List<Integer> statusList = new ArrayList<>();
        statusList.add(1);
        statusList.add(2);
        statusList.add(3);
        Query query = new Query().addCriteria(Criteria.where("status").in(statusList));
        List<Version> versions = mongoBaseDao.find(query, Version.class, "work_version");
        Map<Integer, List<Version>> statusMap = versions.stream().collect(Collectors.groupingBy(Version::getStatus));
        Map<Integer, List<String>> statusIdMap = transMap(statusMap);

        grddingList.forEach(featureEntity -> {
            Version temp1 = new Version();
            temp1.setDate(DateUtil.date2String("yyyy-MM-dd HH:mm:ss"));
            temp1.setGrddingId(featureEntity.getId());
            temp1.setStatus(1);
            temp1.setId(StringUtils.generateUUID());
            if (statusIdMap.get(1) == null || !statusIdMap.get(1).contains(featureEntity.getId())) {
                inserts.add(temp1);
            }

            Version temp2 = new Version();
            BeanUtils.copyProperties(temp1, temp2);
            temp2.setId(StringUtils.generateUUID());
            temp2.setStatus(2);
            if (statusIdMap.get(2) == null || !statusIdMap.get(2).contains(featureEntity.getId())) {
                inserts.add(temp2);
            }

            Version temp3 = new Version();
            BeanUtils.copyProperties(temp1, temp3);
            temp3.setId(StringUtils.generateUUID());
            temp3.setStatus(3);
            if (statusIdMap.get(3) == null || !statusIdMap.get(3).contains(featureEntity.getId())) {
                inserts.add(temp3);
            }
        });
        mongoBaseDao.insertAll(inserts, "work_version");
        return AjaxType.success("");
    }

    private Map<Integer, List<String>> transMap(Map<Integer, List<Version>> statusMap) {
        Map<Integer, List<String>> out = new HashMap<>();
        if (statusMap == null) {
            return out;
        }
        statusMap.keySet().forEach(i -> {
            out.put(i, statusMap.get(i).stream().map(Version::getGrddingId).collect(Collectors.toList()));
        });
        return out;
    }

}
