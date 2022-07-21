package com.hm.tzgis.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoadClassifyDevice implements Serializable {

    private static final long serialVersionUID = 6430167126247129678L;
    private Integer objType;

    private Integer objId;

    private Integer baseObjType;

    private Integer baseObjId;

    private Integer worlid;

    private Integer groupid;

    private String name;


}
