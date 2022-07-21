package com.hm.tzgis.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

/**
 * @author 陈高梁
 * @data 2021年3月15日 -- 下午2:21:02
 * @describe
 *
 */
@Data
public class MongoObjectEntity {
	@Field("type")
    private String type;//Feature
    @Field("geometry")
    private Map<String,String> geometry;
    @Field("properties")
    private Map<String,String> properties;//Json数据
   
}
