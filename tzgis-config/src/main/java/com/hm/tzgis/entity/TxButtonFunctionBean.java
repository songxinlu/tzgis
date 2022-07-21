package com.hm.tzgis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*按钮功能配置*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TxButtonFunctionBean extends PageBean { // 按钮基础表
    private Integer id;//主键ID
    private String buttonCode;//按钮代码（= sys_propertydicts.interval）
    private String descr;//按钮描述（= sys_propertydicts.displayname）
    private String buttonName;//按钮名称（= sys_propertydicts.displayname） ,也可自定义名称
    private String shortcutKey;//快捷键
    private String title;//功能标题
    private String content;//功能介绍
    private String suggestion;//建议
    private String createTime;//创建时间
    private String createUser;//创建人
    private String remark;//备注
    private String isenabled;//是否启用（1-启用，0-不启用）

    private String type;//按钮分类（= SYS_PROPERTYDICTS.INTERVAL ，20）
    private String typeName;//所属分类名称（= SYS_PROPERTYDICTS.DISPLAYNAME）
    private String objType;//设备类型ID(多个时，中间用英文逗号分隔)
    private String objTypeName;//设备类型名称
    private String mapType;//图类型ID(多个时，中间用英文逗号分隔)
    private String mapTypeName;//图类型名称
    //以下，2021-4-19新增字段
    private String icoName;//按钮图标名称
    private String imgName;//按钮图片名称（IMG）
    private String isInputText;//是输入文本（0-否，1-是），默认为0
    private String isDisplayText;//是显示文本（0-否，1-是），默认为0
    private String width;//显示宽度（单位像素）
    private String heigth;//显示高度(单位：像素)
    private String parentButtonCode;//所属父按钮代码（注：子按钮下拉显示）
    private String apiJs;//jsAPI前端接口
    private String apiUrl;//后端接口URL(微服务)
    private String placeholder;//占位符文字（IS_INPUT_TEXT =1 时，输入文本框中显示的文字)
    private String displayTextWords;//显示文本的文字（IS_DISPLAY_TEXT =1 时）
    private String symbolType;//图元符号类型（多个时，英文逗号分隔）（根据画图工具实际定义）
    private String displayFunction;//是显示功能窗口（1-是，0-否）（默认为1）

    private List<TxFileLogBean> fileLogs;

    public TxButtonFunctionBean(String isenabled) {
        this.isenabled = isenabled;
    }
}
