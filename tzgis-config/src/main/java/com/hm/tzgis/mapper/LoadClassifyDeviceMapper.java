package com.hm.tzgis.mapper;

import com.hm.tzgis.entity.LoadGroupInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LoadClassifyDeviceMapper {


//    @Select("select groupid,groupname,sumcapacity,worlid from em.load_group_info where groupid in (select groupid from EM.LOAD_CLASSIFY_DEVICE where worlid = #{worldId} and obj_id = #{objId} and obj_type = #{objType}) and worlid = #{worldId}")
//    List<LoadGroupInfo> find(@Param("worldId") Integer worldId, @Param("objId") Integer objId, @Param("objType") Integer objType);


    @Select("select b.groupid,a.groupname,a.sumcapacity,a.worlid,b.obj_type,b.obj_id from em.load_group_info a, EM.LOAD_CLASSIFY_DEVICE b where a.groupid = b.groupid and b.worlid = #{worldId} and b.obj_id = #{objId} and b.obj_type = #{objType} and a.worlid = #{worldId}")
    List<LoadGroupInfo> find(@Param("worldId") Integer worldId, @Param("objId") Integer objId, @Param("objType") Integer objType);


}
