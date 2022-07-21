package com.hm.tzgis.entity;

import lombok.Data;

/*文件上传日志*/
@Data
public class TxFileLogBean extends PageBean{
  private Integer id;//主键ID
  private String tableCode;//表代码
  private String tableKeyValue;//表主键值
  private String descr;//功能描述
  private String type;//文件业务类型
  private String fileformat;//文件格式
  private String filename;//文件名称
  private String filesize;//文件大小(kb)
  private String filepath;//附件相对路径（或原始图片）
  private String filepathSmall;//附件相对路径（图标图片）（几十kb）
  private String filepathView;//附件相对路径（预览图片）（几百kb）
  private String uploadTime;//上传时间
  private String uploadUser;//上传人名称
  private String companyId;//公司ID
  private String companyName;//公司名称
  private String remark;//备注
}
