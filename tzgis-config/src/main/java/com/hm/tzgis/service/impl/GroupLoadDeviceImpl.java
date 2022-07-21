package com.hm.tzgis.service.impl;

import com.hm.tzgis.entity.GroupLoadDevice;
import com.hm.tzgis.mapper.GroupLoadDeviceMapper;
import com.hm.tzgis.service.GroupLoadDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GroupLoadDeviceImpl implements GroupLoadDeviceService {

    @Autowired
    private GroupLoadDeviceMapper groupLoadDeviceMapper;

    @Override
    public List<GroupLoadDevice> findFhDetail(Integer worldId, Integer groupId) {
        return groupLoadDeviceMapper.findFhDetail(worldId,groupId);
    }


}
