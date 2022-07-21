package com.hm.tzgis.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ZtTypeName implements Serializable {

    String id;

    String devicetype;

    String modelName;

    //是否是线路   1线路用线   0  用图元
    String isLine;

    //是否需要计算角度 1是0否
    String calCangle;

    //图元弧度
    String radian;

    String datatype;

    @Override
    public String toString() {
        return "{" +
                "devicetype='" + devicetype + '\'' +
                ", modelName='" + modelName + '\'' +
                '}';
    }
}
