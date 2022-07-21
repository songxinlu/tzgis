package com.hm.tzgis.entity;

import java.util.List;

import lombok.Data;

@Data
public class SelectReq {
    private String id;
    private String lineId;//线路Id
    private String objectId;//对象id
    private String geoJsonData;//Json数据
    private String cimData;//Json数据
    private String mapType;//275简图  264 详图
    private String fileType;//svg;xml;geo
    private String tableName;
    private String worldId;
    private String modelId;
    private String baseObjId;
    private String objType;
    private String versionType;
    private List<String> tableList;
}
