package com.hm.tzgis.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.tzgis.entity.*;
import com.hm.tzgis.mapper.SysModelMapper;
import com.hm.tzgis.service.SysModelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 模型配置信息(SysModel)表服务实现类
 *
 * @author Peng
 * @since 2021-02-05 17:00:06
 */
@Service
public class SysModelServiceImpl implements SysModelService {

    @Resource
    private SysModelMapper sysModelMapper;

    /**
     * 分页查询
     *
     * @param sysModel
     * @return
     */
    @Override
    public List<SysModel> querySysModel(SysModel sysModel) {
        return sysModelMapper.querySysModel(sysModel);
    }

    /**
     * 分页查询
     *
     * @param sysModelSymbol
     * @return
     */
    @Override
    public List<SysModelSymbol> querySysModelSymbol(SysModelSymbol sysModelSymbol) {
        return sysModelMapper.querySysModelSymbol(sysModelSymbol);
    }

    /**
     * 分页查询
     *
     * @param sysSymbolJson
     * @return
     */
    @Override
    public List<SysSymbolJson> querySysSymbolJson(SysSymbolJson sysSymbolJson) {
        return sysModelMapper.querySysSymbolJson(sysSymbolJson);
    }

    /**
     * 分页查询
     *
     * @param sysDispConfig
     * @return
     */
    @Override
    public List<SysDispConfig> querySysDispConfig(SysDispConfig sysDispConfig) {
        return sysModelMapper.querySysDispConfig(sysDispConfig);
    }

    @Override
    public TxMapVersion queryByWorldid(TxMapVersion txMapVersion) {
        return sysModelMapper.queryByWorldid(txMapVersion);
    }

    @Override
    public List<SysSketchmapPara> querySysSketchmapPara(SysSketchmapPara record) {
        return sysModelMapper.querySysSketchmapPara(record);
    }

    @Override
    public List<SysUniverseConfig> querySysUniverseConfig(SysUniverseConfig sysUniverseConfig) {
        return sysModelMapper.querySysUniverseConfig(sysUniverseConfig);
    }

    public SysEnvironment querySysEnvironmentBySection(String companyId,String section) {
        List<SysEnvironment> list = sysModelMapper.querySysEnvironmentBySection(companyId,section);
        if(list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<GisSysColorBean> getGisSysColorByDispType(Long dispType){
        return sysModelMapper.getGisSysColorByDispType(dispType);
    }

    @Override
    public Map queryTreeTxButtonConfig() {
        List<TxButtonConfigBean> configs = sysModelMapper.queryAllTxButtonConfig();
        List<TxButtonFunctionBean> functions = sysModelMapper.queryTxButtonFunction(null);
        List<String> internalIds = configs.stream().map(TxButtonConfigBean::getType).distinct().collect(Collectors.toList());
        List<String> btnCodes = functions.stream().map(TxButtonFunctionBean::getButtonCode).collect(Collectors.toList());
        List<TxPropertydicts> properties = sysModelMapper.queryPropertiesByInternalIds(internalIds);
        List<TxFileLogBean> fileLogs = sysModelMapper.queryFileLogBeanByButtonCode(btnCodes);
        List<String> propOrderedList = properties.stream()
                .sorted(Comparator.comparing(c -> Integer.valueOf(c.getDisplayseq())))
                .map(TxPropertydicts::getInternalid)
                .collect(Collectors.toList());
        //为function添加fileLog
        functions.forEach(f -> f.setFileLogs(fileLogs.stream()
                .filter(l -> l.getTableKeyValue().equals(String.valueOf(f.getId())))
                .collect(Collectors.toList())));
        //为每一个btnConfig添加btnFunction
        // 注： functions 对应库表的每个ButtonCode 是唯一的， configs 对应库表 的 ButtonCode 可以有多个相同的记录
        configs.forEach(c -> c.setFunction(functions.stream()
                .filter(f -> c.getButtonCode().equals(f.getButtonCode()))
                .findFirst().orElse(new TxButtonFunctionBean())));
        HashMap<String, Object> result = Maps.newHashMap();
        //1、根据btnConfig的buttonClass属性分组
        Map<String, List<TxButtonConfigBean>> configMap = configs.stream()
                .sorted(Comparator.comparing(TxButtonConfigBean::getDisplayNum, Comparator.nullsFirst(Comparator.naturalOrder())))
                .collect(Collectors.groupingBy(TxButtonConfigBean::getButtonClass));
        //2、对每个buttonClass分组继续根据type属性分组，同时对type分组构建树状结构
        configMap.forEach((key, value) -> {
            Map<String, List<TxButtonConfigBean>> type = value
                    .stream().collect(Collectors.groupingBy(TxButtonConfigBean::getType));
            type.entrySet().forEach(ee -> {
                List<TxButtonConfigBean> newValue = ee.getValue().stream()
                        .filter(c -> null == c.getParentButtonCode())
                        .peek(c -> c.setChildren(getChildren(c, ee.getValue())))
                        .collect(Collectors.toList());
                ee.setValue(newValue);

            });
//            HashMap<String, Object> level2map = Maps.newHashMap();
            ArrayList<Map> level2list = Lists.newArrayList();
            type.forEach((k, v) -> {
                HashMap<String, Object> cell = Maps.newHashMap();
                TxPropertydicts property = properties.stream()
                        .filter(t -> t.getInternalid().equals(k))
                        .findFirst()
                        .orElse(new TxPropertydicts());
                cell.put("internalid", property.getInternalid());
                cell.put("displayname", property.getDisplayname());
                cell.put("ispopup", property.getIspopup());
                cell.put("configs", v);
//                level2map.put(k, cell);
                level2list.add(cell);
            });
            level2list.sort(Comparator.comparing(item -> propOrderedList.indexOf(item.get("internalid"))));
            result.put(key, level2list);
        });
        return result;
    }

    private List<TxButtonConfigBean> getChildren(TxButtonConfigBean root, List<TxButtonConfigBean> all) {
        List<TxButtonConfigBean> list = all.stream()
                .filter(c -> null != c.getParentButtonCode() && Integer.valueOf(c.getParentButtonCode()).equals(root.getId()))
                .peek(c -> c.setChildren(getChildren(c, all)))
                .collect(Collectors.toList());
        return list;
    }

}
