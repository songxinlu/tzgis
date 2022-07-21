package com.hm.tzgis.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author 陈高梁
 * @data 2021年3月17日 -- 下午2:09:25
 * @describe
 *
 */
@Data
public class MongoMapEntity {
	@Field("worldid")
	private String worldid;
	@Field("universe")
	private String universe;
	@Field("worldname")
	private String worldname;
	@Field("baseobjid")
	private String baseobjid;
	@Field("baseobjtype")
	private String baseobjtype;
	@Field("viewbox")
	private String viewbox;
	@Field("objNum")
	private String objNum;
	@Field("objTypes")
	private String objTypes;
	@Field("relationlines")
	private String relationlines;
}
