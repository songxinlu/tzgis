package com.hm.tzgis.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hm.tzgis.dao.MangoBaseDao;
import com.hm.tzgis.entity.SelectReq;
import com.hm.tzgis.util.CommonResult;
import com.hm.tzgis.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class PatternController {

    @Autowired
    private MangoBaseDao mangoBaseDao;


    //根据图示id获取专题图数据
    @PostMapping("queryGeoJsonByWorldId")
    public CommonResult<Map<String, Object>> queryGeoJsonByWorldId(@RequestBody SelectReq entity) {
        String geoJsonData = mangoBaseDao.getAllGeo(entity.getWorldId());
        JSONObject json = JSONArray.parseObject(geoJsonData);
        if(!StringUtils.isEmpty(entity.getBaseObjId())) {
            //添加高亮显示的设备
            Map<String, Object> map1 = new HashMap<>();
            map1.put("base_obj_id", entity.getBaseObjId());
            map1.put("modelid", entity.getModelId());
            json.put("locate_obj", map1);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("geoJsonData", json);
        return CommonResult.success(map);
    }

}
