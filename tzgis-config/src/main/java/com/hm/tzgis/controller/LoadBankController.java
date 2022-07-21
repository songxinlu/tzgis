package com.hm.tzgis.controller;

import com.alibaba.fastjson.JSONObject;
import com.hm.tzgis.entity.AjaxType;
import com.hm.tzgis.entity.GroupLoadDevice;
import com.hm.tzgis.entity.LoadGroupInfo;
import com.hm.tzgis.service.GroupLoadDeviceService;
import com.hm.tzgis.service.LoadClassifyDeviceService;
import com.hm.tzgis.service.LoadGroupInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 负荷组查询
 */
@RestController
@CrossOrigin(origins = "*")
public class LoadBankController {

    @Autowired
    private LoadClassifyDeviceService loadClassifyDeviceService;

    @Autowired
    private LoadGroupInfoService loadGroupInfoService;

    @Autowired
    private GroupLoadDeviceService groupLoadDeviceService;


    @PostMapping("/findFhList")
    public AjaxType findFhList(@RequestBody JSONObject jsonObject) {
        if (null == jsonObject.getInteger("worldId")) {
            return new AjaxType("200", false, "worldId is null", null);
        }
        if (null == jsonObject.getInteger("objId") && null == jsonObject.getInteger("objType")) {
            List<LoadGroupInfo> list = loadGroupInfoService.findAllFhZu(jsonObject.getInteger("worldId"));
            return new AjaxType("200", true, "success", list);
        }
        List<LoadGroupInfo> list = loadClassifyDeviceService.find(jsonObject.getInteger("worldId"), jsonObject.getInteger("objId"), jsonObject.getInteger("objType"));
        return new AjaxType("200", true, "success", list);
    }

    @PostMapping("/findFhDetail")
    public AjaxType findFhDetail(@RequestBody JSONObject jsonObject) {
        if (null == jsonObject.getInteger("groupId")) {
            return new AjaxType("200", false, "groupId is null", null);
        }
        if (null == jsonObject.getInteger("worldId")) {
            return new AjaxType("200", false, "worldId is null", null);
        }
        List<GroupLoadDevice> list = groupLoadDeviceService.findFhDetail(jsonObject.getInteger("worldId"), jsonObject.getInteger("groupId"));
        return new AjaxType("200", true, "success", list);
    }
}
