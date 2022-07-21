package com.hm.tzgis.entity;

import lombok.Data;

import java.util.List;

/**
 * @author 陈高梁
 * @data 2021年4月6日 -- 上午10:01:24
 * @describe 专题图菜单/设备类型-按钮分类
 */
@Data
public class TxButtonConfigBean extends PageBean {
    private Integer id;
    private String buttonCode;//按钮代码
    private String descr;//按钮描述
    private String buttonName;//按钮名称
    private String type;//所属分类
    private String typeName;//所属分类名称
    private String createTime;//创建时间
    private String createUser;//创建人
    private String updateTime;//修改时间
    private String updateUser;//修改人
    private String remark;//备注
    private String isenabled;//是否启用（1-启用，0-不启用），默认显示1
    private String isdisplay;//是否显示（1-显示，0-隐藏），默认显示1
    //2021-4-19新增
    private String parentButtonCode;//所属父按钮代码（注：子按钮下拉显示）
    private String displayNum;//显示排序（从左到右 或 从上到下）
    private String displayRowNum;//显示在第几行（超级菜单时用）
    private String ispopup;//是下拉菜单按钮（1-是，0-否），默认0
    private String isdefaultSelectButton;//是默认选择按钮代码（1-是，0-否），默认0
    private String buttonClass;//按钮分类(normal -普通菜单，super - 超级菜单，popup -右键菜单)，默认normal
    private String hasRightLine;//有按钮右边间隔线(1-有，0-无)，默认为0
    private String groupIndex;//分组号（0,1,2,3）【用于右键菜单的分组的横线间隔】
    

    private List<TxButtonConfigBean> children;
    private TxButtonFunctionBean function;
}
