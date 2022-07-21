package com.hm.tzgis.entity;

import lombok.Data;

import java.util.Date;

/**
 * (SysSymbolJson)实体类
 *
 * @author Peng
 * @since 2021-02-05 17:15:39
 */
@Data
public class SysSymbolJson {

    private Integer id;

    //符号名称
    private String name;

    private String code;

    //状态 0 成功 1 失败 2 警告 3 异常
//    private Integer status;

    //操作时间
//    private Date oprttime;

    //错误信息
//    private String errinfo;

    //对象符号描述
    private String dataclob;

}
