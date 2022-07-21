package com.hm.tzgis.entity;

import lombok.Data;

@Data
public class SysEnvironment {
	private Integer id;
	private String sectionName;
	private String keyName;
	private String val;
	private String fieldesc;
	private String remark;
	private Integer enabled;
	private Integer cateid;
	private String insize;
	private Integer valuetype;
	private Integer areaId;
}
