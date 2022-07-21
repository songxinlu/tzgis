package com.hm.tzgis.entity;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lxj
 * @date 2021/3/11 13:12
 */
@Data
public class Geometry implements Serializable {

    //类型
    String type;

    //点
    JSONArray coordinates;
}
