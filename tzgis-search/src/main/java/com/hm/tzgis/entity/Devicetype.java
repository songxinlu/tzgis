package com.hm.tzgis.entity;

import lombok.Data;

/**
 * @author shike
 * @date 2022/4/21 15:50
 */
@Data
public class Devicetype {

    String devicetype;

    /**
     * 1配网设备 2变电站 3变电设备
     */
    String type;

    String modelName;
}
