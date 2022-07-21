package com.hm.tzgis.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author shike
 * @date 2022/5/9 15:50
 */
@Data
public class Version {

    @Field("id")
    String id;

    @Field("name")
    String name;

    @Field("desc")
    String desc;

    @Field("grddingId")
    String grddingId;


    @Field("versionId")
    String versionId;

    @Field("projectId")
    String projectId;

    @Field("status")
    int status;

    @Field("date")
    String date;
}
