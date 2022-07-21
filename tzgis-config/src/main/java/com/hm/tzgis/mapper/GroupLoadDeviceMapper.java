package com.hm.tzgis.mapper;

import com.hm.tzgis.entity.GroupLoadDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupLoadDeviceMapper {


    @Select("select * from em.groupload_device where groupid = #{groupId} and worlid = #{worldId}")
    List<GroupLoadDevice> findFhDetail(@Param("worldId") Integer worldId, @Param("groupId") Integer groupId);


}
