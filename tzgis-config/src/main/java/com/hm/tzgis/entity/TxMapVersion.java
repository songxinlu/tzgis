package com.hm.tzgis.entity;

import lombok.Data;
import java.util.List;



@Data
public class TxMapVersion extends PageBean {

  private Integer id;//主键
  private String companyId;//公司ID
  private String companyName;//公司名称
  private String yxdwId;//运行单位ID
  private String yxdwName;//运行单位名称
  private String bdzId;//变电站ID
  private String bdzName;//变电站名称
  private String lineId;//线路id
  private String lineName;//线路名称
  private String voltageId;//电压等级id
  private String voltageName;//电压等级名称
  private String objectType;//设备类型id
  private String objectTypeName;//设备类型名称
  private String objectId;//设备ID
  private String objectName;//设备名称
  private String mapTypeId;//图类型id
  private String mapTypeName;//图类型名称
  private String flowId;//异动流程ID
  private String flowBusinessId;//异动流程业务ID
  private String versionType;//版本类型(0-黑图，1-历史图，2-红图)
  private String mapState;//图纸状态：异动单的图纸状态：
  private String mapVersion;//版本号（时间戳,毫秒整数）
  private String fileGeoId;//文件GEO主键值（MONGODB）
  private String worldId;//em图纸ID
  private String mapName;//图纸名称
  private String filenameSvg;//文件名SVG
  private String filepathSvg;//文件路径SVG
  private String filenameXml;//文件名XML
  private String filepathXml;//文件路径XML
  private String filenameGeo;//文件名GEO
  private String filepathGeo;//文件路径GEO
  private String remark;//备注
  private String createTime;//创建时间
  private String createDeptId;//创建部门ID
  private String createDeptName;//创建部门名称
  private String createUserId;//创建人ID
  private String createUserName;//创建人名称
  
  private String publishDate;//版本发布日期（转为黑图时）
  private String baseWorldid;// 基准图纸（老版本）（= EM.GIS_SYS_WORLD.BASE_WORLDID）
  
	/*方便库表记录修改 及 删除，  库表代码：按年月命名， 动态的，如 TX_MAP_VERSION_202101 */
  
  private String tableCode;// 辅助字段： 库表代码
  private String objNum;// 辅助字段： 设备数量
  private String viewbox;// 辅助字段： 图纸范围
  
  
  private String startDate;// 搜索条件，开始日期
  
  private String endDate;// 搜索条件，结束日期
  
  private List<String> tableList;//用到的表名集合

  private String isRubbish;//0全部，1：垃圾图纸
  
  private String sourceType;//图纸来源
  
}
