package com.hm.tzgis.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data
public class FeatureEntity implements Serializable {

    @Field("id")
    String id;

    @Field("geometry")
    Object geometry;//坐标

    @Field("properties")
    Propertie properties;

    @Field("FNode")
    String FNode;

    @Field("TNode")
    String TNode;

    @Field("type")
    String type;//类型


}