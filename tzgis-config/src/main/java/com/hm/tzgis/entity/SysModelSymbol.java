package com.hm.tzgis.entity;

import lombok.Data;

/**
 * 对象图元配置(SysModelSymbol)实体类
 *
 * @author Peng
 * @since 2021-02-05 17:11:09
 */
@Data
public class SysModelSymbol{

    //ID
    private Integer id;

    //图元名称
    private Integer worldid;

    //对象名称
    private Integer objType;

    //显示顺序
//    private Integer dispOrder;

    //子类型
    private Integer subType;

    //是否显示
//    private Integer display;

    //IMG图形索引号
//    private Integer imgIndex;

}
