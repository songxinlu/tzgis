package com.hm.tzgis.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class LoadGroupInfo implements Serializable {

    private static final long serialVersionUID = 6430167112247039678L;
    private Integer groupid;

    private String groupname;

    private Integer sumcapacity;//负荷容量总计

    private Integer worlid;

    private Integer sumload;    //负荷

    private Integer supply_radius; //供电半径

    private String sumlength;//长度

    private String model;//型号

    private List<Map> classList;

    private String obj_type;

    private String obj_id;

}
