package com.hm.tzgis.service;

import com.hm.tzgis.entity.*;

import java.util.List;
import java.util.Map;

/**
 * 模型配置信息(SysModel)表服务接口
 *
 * @author Peng
 * @since 2021-02-05 17:00:06
 */
public interface SysModelService {

    /**
     * 分页查询
     *
     * @param sysModel
     * @return
     */
    List<SysModel> querySysModel(SysModel sysModel);

    /**
     * 分页查询
     *
     * @param sysModelSymbol
     * @return
     */
    List<SysModelSymbol> querySysModelSymbol(SysModelSymbol sysModelSymbol);

    /**
     * 分页查询
     *
     * @param sysSymbolJson
     * @return
     */
    List<SysSymbolJson> querySysSymbolJson(SysSymbolJson sysSymbolJson);

    /**
     * 分页查询
     *
     * @param sysDispConfig
     * @return
     */
    List<SysDispConfig> querySysDispConfig(SysDispConfig sysDispConfig);

    /**
     *
     * @author 陈高梁
     * @data 2021年3月18日 -- 上午11:02:33
     * @describe 根据worldid查询数据
     *
     */
    TxMapVersion queryByWorldid(TxMapVersion txMapVersion);

    List<SysSketchmapPara> querySysSketchmapPara(SysSketchmapPara record);

    List<SysUniverseConfig> querySysUniverseConfig(SysUniverseConfig sysUniverseConfig);

    /**
     * 根据单位获取配置的属性
     * @param companyId
     * @return
     */
    SysEnvironment querySysEnvironmentBySection(String companyId,String section);

    /**
     *
     * @describe  根据类型获取颜色数据
     */
    public List<GisSysColorBean> getGisSysColorByDispType(Long dispType);

    Map queryTreeTxButtonConfig();
}
