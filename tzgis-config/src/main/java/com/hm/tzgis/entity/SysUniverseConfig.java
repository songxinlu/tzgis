package com.hm.tzgis.entity;

import lombok.Data;

/**
 * @author 陈高梁
 * @data 2021年12月7日 -- 下午5:49:53
 * @describe
 *
 */
@Data
public class SysUniverseConfig {

	private Long id;
	private String name;
	private Integer orderno;
	private Integer enabled;
	private Integer dispType;
	private Integer coordType;
	private Integer refExtentType;
	private Double refExtentL;
	private Double refExtentR;
	private Double refExtentT;
	private Double refExtentB;
	private Double crossRadius;
	private Integer typeFlag;
	private Integer dispFlag;
	private Integer dispBgFlag;
	private Integer coreFlag;	
	private Integer scadaFlag;
	private Integer miscFlag;
	private Integer operFlag;
	private Integer flag1;
	private Integer flag2;
	private Integer flag3;
	
	
}
