package com.hm.tzgis.mapper;

import com.hm.tzgis.entity.LoadGroupInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface LoadGroupInfoMapper {

    @Select("select groupid,groupname,sumcapacity,worlid,sumload,supply_radius,sumlength,model from em.load_group_info where worlid = #{worldId}")
    List<LoadGroupInfo> findAllFhZu(@Param("worldId") Integer worldId);

    @Select("select obj_id, obj_type from em.load_classify_device where groupid = #{groupid}")
    List<Map> findClassify(@Param("groupid") Integer groupid);
}
 