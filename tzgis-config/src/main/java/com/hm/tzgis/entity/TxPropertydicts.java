package com.hm.tzgis.entity;

import lombok.Data;

import java.util.List;

/**
 * 枚举定义
 */
@Data
public class TxPropertydicts {
    private Integer id;
    private Integer propertydomainid;//分类id
    private String internalid;//系统内置ID
    private String displayseq;//显示顺序
    private String displayname;//属性名称
    private String simplepinyin;//简拼
    private String fullpinyin;//全拼
    private String isdisable;//是否停用，0：否，1：是
    private String createTime;//创建时间
    private String createUser;//创建人
    private String remark;//备注
    private String ispopup;//是下拉菜单按钮（1-是，0-否），默认0
    private List<TxButtonConfigBean> configs;
}
