package com.hm.tzgis.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.hm.tzgis.dao.MangoBaseDao;
import com.hm.tzgis.entity.AjaxType;
import com.hm.tzgis.entity.Devicetype;
import com.hm.tzgis.entity.FeatureEntity;
import com.hm.tzgis.entity.Version;
import com.hm.tzgis.entity.WorkVersion;
import com.hm.tzgis.mapper.WtkMapper;
import com.hm.tzgis.util.CommonResult;
import com.hm.tzgis.util.DateUtil;
import com.hm.tzgis.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author shike
 * @date z/5/26 16:54
 */
@RestController
@CrossOrigin(origins = "*")
public class EditController {

    @Autowired
    private WtkMapper wtkMapper;

    @Autowired
    private MangoBaseDao mongoBaseDao;

    /**
     * editFlag  0基础 1新增 2修改 3删除
     * status 0：现状；1:规划推演；2：储备推演；3：计划推演
     *
     * @param json
     * @return
     */
    @RequestMapping("editDeviceData")
    public CommonResult editDeviceData(@RequestBody JSONObject json) {

        JSONArray deviceList = json.getJSONArray("zttobjs");
        if (!json.keySet().contains("status")) {
            return CommonResult.failed("status状态为空");
        }
        int status = json.getInteger("status");
        for (int i = 0; i < deviceList.size(); i++) {
            JSONObject device = deviceList.getJSONObject(i);

            String editflag = device.getString("editflag");
            String devicetype = device.getJSONObject("properties").getString("devicetype");
            switch (status) {
                case 0:
                    try {
                        device.getJSONObject("properties").put("objtype", devicetype);
                        if ("add".equals(editflag)) {
                            mongoBaseDao.insert(device, devicetype);

                        } else if ("update".equals(editflag)) {
                            mongoBaseDao.deleteById(device.getString("id"), JSONObject.class, devicetype);
                            mongoBaseDao.insert(device, devicetype);

                        } else if ("delete".equals(editflag)) {
                            Query query = new Query(Criteria.where("id").is(device.getString("id")));
                            Update update = new Update().set("properties.objoprtflag", "3");
                            mongoBaseDao.updateMulti(query, update, devicetype);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1://规划推演
                    editByDevice(device,editflag,devicetype,status);
                    break;
                case 2://储备推演
                    editByDevice(device,editflag,devicetype,status);
                    break;
                case 3://计划推演
                    editByDevice(device,editflag,devicetype,status);
                    break;
                default:
                    break;
            }


        }

        return CommonResult.success("");
    }

    /**
     * @Description 更新推演数据
     * @Method editByDevice
     * @Author songxl
     * @param device
     * @param editflag  0基础 1新增 2修改 3删除
     * @param devicetype  设备类型
     * @param status 0：现状；1:规划推演；2：储备推演；3：计划推演
     * @Return void
     */
    private void editByDevice(JSONObject device, String editflag, String devicetype, int status) {

        device.getJSONObject("properties").put("status", status);
        device.getJSONObject("properties").put("objtype", devicetype);
        if ("add".equals(editflag) || "delete".equals(editflag)) {
            mongoBaseDao.insert(device, "edit_record");
        } else if ("update".equals(editflag)) {
            Query queryWhere = new Query(Criteria.where("properties.gridding_id").is(device.getJSONObject("properties").getString("gridding_id"))//网格
                    .and("properties.objtype").is(device.getJSONObject("properties").getString("objtype"))//对象类型
                    .and("properties.project_id").is(device.getJSONObject("properties").getString("project_id"))//项目id
                    .and("status").is(status));//哪一状态
            mongoBaseDao.deleteByWhere(queryWhere, JSONObject.class, "edit_record");
            mongoBaseDao.insert(device, "edit_record");
        }
        //修改规划推演对应网格的版本时间
        upWorkVersionTime(device,status);
    }


    /**
     * @Description修改对应网格、对应阶段数据的版本时间
     * @Method upWorkVersionTime
     * @Author songxl
     * @param device
     * @param status 0：现状；1:规划推演；2：储备推演；3：计划推演
     * @Return void
     */
    private void upWorkVersionTime(JSONObject device, int status) {
        JSONObject properties = device.getJSONObject("properties");
        if(!properties.keySet().contains("gridding_id")||!properties.keySet().contains("status")){
            throw new NullPointerException("gridding_id或status为空");
        }
        Query query = new Query(Criteria.where("grddingId").is(properties.getString("gridding_id"))
                .and("status").is(status));
        Update update = new Update().set("date", DateUtil.date2String("yyyy-MM-dd HH:mm:ss"));
        mongoBaseDao.updateMulti(query, update, "work_version");

    }


    @RequestMapping("queryWkBySbmc")
    public AjaxType queryWkBySbmc(@RequestParam("wtlx") String wtlx, @RequestParam("sbmc") String sbmc) {
        return new AjaxType("200", true, "success", wtkMapper.queryWkBySbmc(wtlx, sbmc));
    }

}
