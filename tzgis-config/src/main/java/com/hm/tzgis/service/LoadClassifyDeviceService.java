package com.hm.tzgis.service;

import com.hm.tzgis.entity.LoadGroupInfo;

import java.util.List;


public interface LoadClassifyDeviceService {

    List<LoadGroupInfo> find(Integer worldId, Integer objId, Integer objType);

}


