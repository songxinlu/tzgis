package com.hm.tzgis.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @author lxj
 * @date 2021/2/21 12:45
 */
@Data
public class Model implements  Serializable  {

    @Field("id")
    String id;

    @Field("geometry")
    Geometry geometry;//坐标

    @Field("type")
    String type;//类型

    @Field("properties")
    JSONObject jo;

    //4位geohash
    @Field("hashcode4")
    String hashcode4;

    //6位geohash
    @Field("hashcode5")
    String hashcode5;

    //6位geohash
    @Field("hashcode6")
    String hashcode6;

    //7位geohash
    @Field("hashcode7")
    String hashcode7;

    @Field("apptype")
    String apptype;

    @Field("yxdw")
    String yxdw;

    @Field("name")
    String name;

    @Field("ssdkx")
    String ssdkx;

    @Field("ptype")
    String ptype;

    @Field("pid")
    String pid;

    @Field("devicetype")
    String devicetype;

    @Field("datatype")
    String datatype;

    //安徽二期使用
    @Field("oid")
    String oid;
}
