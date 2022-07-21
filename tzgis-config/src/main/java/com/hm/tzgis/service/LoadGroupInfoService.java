package com.hm.tzgis.service;

import com.hm.tzgis.entity.LoadGroupInfo;

import java.util.List;


public interface LoadGroupInfoService {

     List<LoadGroupInfo> findAllFhZu(int worldId);
}


