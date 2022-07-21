package com.hm.tzgis.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author shike
 * @date 2022/4/2 15:26
 */
public interface SearchService {


    public JSONObject getGeojson(String gridding, List<String> project_id);
}
