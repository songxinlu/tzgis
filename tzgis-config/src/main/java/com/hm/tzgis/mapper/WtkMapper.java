package com.hm.tzgis.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WtkMapper {

    List<Map> queryWkBySbmc(String wtlx, String sbmc);
}