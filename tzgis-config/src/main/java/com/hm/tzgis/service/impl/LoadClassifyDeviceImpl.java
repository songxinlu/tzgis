package com.hm.tzgis.service.impl;

import com.hm.tzgis.entity.LoadGroupInfo;
import com.hm.tzgis.mapper.LoadClassifyDeviceMapper;
import com.hm.tzgis.service.LoadClassifyDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LoadClassifyDeviceImpl implements LoadClassifyDeviceService {

    @Autowired
    private LoadClassifyDeviceMapper loadClassifyDeviceMapper;

    @Override
    public List<LoadGroupInfo> find(Integer worldId, Integer objId, Integer objType) {
        return loadClassifyDeviceMapper.find(worldId, objId, objType);
    }

}
