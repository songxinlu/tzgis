package com.hm.tzgis.service.impl;

import com.hm.tzgis.entity.LoadGroupInfo;
import com.hm.tzgis.mapper.LoadGroupInfoMapper;
import com.hm.tzgis.service.LoadGroupInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LoadGroupInfoImpl implements LoadGroupInfoService {

    @Autowired
    private LoadGroupInfoMapper loadGroupInfoMapper;

    @Override
    public List<LoadGroupInfo> findAllFhZu(int worldId) {


        List<LoadGroupInfo> list  = loadGroupInfoMapper.findAllFhZu(worldId);
        for (LoadGroupInfo loadGroupInfo : list) {
            loadGroupInfo.setClassList(loadGroupInfoMapper.findClassify(loadGroupInfo.getGroupid()));
        }
        return list;
    }

}
