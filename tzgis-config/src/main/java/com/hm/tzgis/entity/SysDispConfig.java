package com.hm.tzgis.entity;

import lombok.Data;

/**
 * 对象显示配置(SysDispConfig)实体类
 *
 * @author Peng
 * @since 2021-02-05 17:20:10
 */
@Data
public class SysDispConfig {

    //主键ID
    private Integer id;

    //设备类型ID
    private Integer objType;

    //分类字段值
    private Integer subtype;

    //最小比例尺
    private Double minScale;

    //最大比例尺
    private Double maxScale;

    //显示大小
    private Integer objSize;

    //显示符号
    private Integer objSym;

    //显示线宽
    private Integer lineWidth;

    //SERVER_ID
    private Integer serverId;

    //分类字段
//    private String subField;

    //显示颜色
    private Integer color;

    //画笔风格
    private Integer penstyle;

    //显示为符号
    private Integer showAsPoint;

    //方案号
//    private Integer schemeId;

    //填充色
    private Integer fillColor;

    //标注比例
//    private Float labelScale;

    //是否显示标注
//    private Float objLabelScale;

    //图形类型
    private Integer universe;

    //显示顺序
//    private Integer ord;

    //RANGEID
//    private Integer rangeid;

    //标注颜色
//    private Integer labelColor;

    private Integer brushType;

    private Integer areaId;
    
    
    //
    //人员所属的供电公司id , ISO
//    private String companyId;
}
