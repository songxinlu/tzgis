package com.hm.tzgis.controller;

import com.hm.tzgis.dao.MangoBaseDao;
import com.hm.tzgis.entity.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shike
 * @date 2022/5/10 13:56
 */

@RestController
public class VersionController {

    @Autowired
    private MangoBaseDao mongoBaseDao;



    @PostMapping("addVersion")
    public void insertVersion(@RequestBody Version version){
        mongoBaseDao.insert(version, "work_version");
    }

}
