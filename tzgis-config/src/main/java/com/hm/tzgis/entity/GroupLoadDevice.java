package com.hm.tzgis.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class GroupLoadDevice implements Serializable {

    private static final long serialVersionUID = 6430167126247039678L;
    private Integer objType;

    private Integer objId;

    private Integer capacity;//负荷容量

    private String name;

    private Integer worldid;

    private Integer groupid;

    private String model;//型号

    private Integer load;//负荷

}
