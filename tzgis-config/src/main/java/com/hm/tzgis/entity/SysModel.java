package com.hm.tzgis.entity;

import lombok.Data;

/**
 * 模型配置信息(SysModel)实体类
 *
 * @author Peng
 * @since 2021-02-05 17:00:04
 */
@Data
public class SysModel{

    //ID
    private Integer id;

    //名称
    private String name;

    //几何类型 0 无几何 1 点 2 多点 3 线型 4 区域 5 文字
    private Integer geoType;

    //是否按WORLDID加载
//    private Integer worldLoadFlag;

    //实例类型
    private Integer modelInst;

    //编辑标志
//    private Integer editFlag;

    //可否作为起始设备
//    private Integer startFlag;

    //是否参与查询统计
//    private Integer queryFlag;

    //网络类型
    private Integer topoNetwork;

    //内控属性(按位) 第1位:标注 第2位:一般属性 第3位:值属性 第4位:遥测 第5位:置数 第6位:挂牌 第7位:方向 第8位:过载
    private Integer realAttibFlag;

    //电系性质 0 通常 1 电源点 2 接地 3 二级电源点 4 线路起始点 5 线路终止点 6 出口开关 7 联络开关 8 出口、联络开关
    private Integer powerAttrib;

    //电气性质 0 一般 1 导线 2 开断 3 变压 4 母线 5 信号量 6 连接线 7 无穷大电阻 8 虚拟杆 9 电缆导线 10 光缆接头
    private Integer electricType;

    //设备分类 1 一次 2 二次 3 三次 4 线路 5 电缆 6 用户 7 打印固件 8 一次图设备
    private Integer deviceCategory;

    //几何属性(固定点数)
//    private Integer fixedPointAttrib;

    //旋转标志
//    private Integer moveRotateFlag;

    //点选规则
//    private Integer pointSelRule;

    //区域选择规则
//    private Integer regionSelRule;

    //复杂边线标志
//    private Integer complexEdgeFlag;

    //分隔标志
//    private Integer splitedFlag;
//
//    //加载操作标志
//    private Integer loadOperFlag;

    //符号规则
    private Integer symbolRule;

    //空间数据源类型
//    private Integer geoDbType;

    //空间数据源连接属性
//    private String geoDbNo;

    //空间数据源表名
//    private String geoTbl;

    //属性数据源类型
//    private Integer attrDbType;

    //属性数据源连接属性
//    private String attrDbNo;

    //属性数据源表名-基表
//    private String attrTbl;

    //同步模式
//    private Integer syncMode;

    //显示顺序
    private Integer dispOrder;

    //是否启用该对象 0 不启用 1 GIS专用 2 EM专用 3 GEO/EM公用
    private Integer enableed;

    //计量单位
//    private Integer unit;

    //状态缺省值
//    private Integer defaultvalue;

    //操作属性
//    private Integer operAttrib;

    //ID序列名
//    private String modelSeq;

    //是否有图形
//    private Integer hasGis;

    //是否参与配对处理
//    private Integer specialBreak;

    //符号缩放规则
//    private Integer symbolZoomRule;

    //别名
//    private String descr;

    //对象缺省电压等级
//    private Integer modelVolt;

    //SCADA采集标志
//    private Integer scadaFlag;

    //是否支持三维
//    private Integer has3d;

    //设备类型
    private Integer deviceType;

    //是否注册版本
//    private Integer modelVerFlag;

    //版本属性表
//    private String attrVerTbl;

    //版本模式
//    private Integer modelVerRule;

    //台帐标记
//    private Integer cardParaFlag;

    //是否专题图对象
    private Integer isSketchObj;

    //拓扑连接类型 0 无 1 一个拓扑点 2 两个拓扑点 3 线性拓扑(如母线)
    private Integer topoType;

    private String fieldsList;

    //备注
//    private String remark;

    //属性关键信息视图
//    private String keyinfoView;

//    private String keyinfoviewBak;

//    private String attrView;

//    private String geoView;

}
