package com.hm.tzgis.service;

import com.hm.tzgis.entity.GroupLoadDevice;

import java.util.List;


public interface GroupLoadDeviceService {

    List<GroupLoadDevice> findFhDetail(Integer worldId, Integer groupId);
}


