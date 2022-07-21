package com.hm.tzgis.entity;

import lombok.Data;

@Data
public class LoginUserBean {
    private String loginName;//登录账号
    private String password;//密码
    private String id; //ID

    private String roleId;//角色id

    private String userId;//人员id

    private String iscId;//ISC 统一权限管理平台 的人员ID 

    private String ssywdwId; //所属运维单位id 

    private Integer pageSize;// 每页显示多少条
    private Integer pageNo; //第几页

    private String path; //单位名称路径
    private String ssywdwmc; //所属运维单位名称 
    private String rymc; //人员名称
    private String dwjb; //单位级别 ： 上级部门的单位级别  ， 803 -运维班组
    private String sjbmid;//上级部门ID
    private String sjbmmc;//上及部门名称
    private String sswsid; //所属网省id ,  省公司
    private String sswsmc; //所属网省名称 ,  省公司
    private String ssdsid; //所属地市id
    private String ssdsmc; //所属地市名称 ， 地市公司
    private String ssgdid; //所属供电公司(县局)， 县公司ID
    private String ssgdmc; //所属供电公司(县局)， 县公司
    private String roleName;//人员角色名称
    private String companyId;//供电公司id 
    
    // 维护班组： 专题图索引模块的 组织机构树的 普通用户的定位，定位某个维护班组 ，折叠 组织结构树区域， 从线路运维班组字段获取
    private String whbzid;//维护班组ID
    private String whbzmc;//维护班组名称
    
    private String ssbmid;//所属部门id
    private String ssbmmc;//所属部门名称
    
    
    private String addrIp;

	 
    

}
