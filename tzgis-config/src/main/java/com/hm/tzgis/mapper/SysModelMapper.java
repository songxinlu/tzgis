package com.hm.tzgis.mapper;

import com.hm.tzgis.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 模型配置信息(SysModel)表数据库访问层
 */
@Mapper
public interface SysModelMapper {

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

    List<SysEnvironment> querySysEnvironmentBySection(String companyId,String section);

    List<GisSysColorBean> getGisSysColorByDispType(Long dispType);

    List<TxButtonConfigBean> queryAllTxButtonConfig();

    List<TxButtonFunctionBean> queryTxButtonFunction(TxButtonFunctionBean txButtonFunctionBean);

    List<TxPropertydicts> queryPropertiesByInternalIds(List<String> internalIds);

    List<TxFileLogBean> queryFileLogBeanByButtonCode(List<String> codes);

    @Select("${sqldata}")
    int sqlExecute(@Param("sqldata") String sqldata);
}

