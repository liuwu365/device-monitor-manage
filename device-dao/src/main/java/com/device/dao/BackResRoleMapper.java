package com.device.dao;

import com.device.api.entity.backrole.BackResRoleKey;
import com.device.api.entity.backrole.BackUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BackResRoleMapper {
    int deleteByPrimaryKey(BackResRoleKey key);

    int insert(BackResRoleKey record);

    int insertSelective(BackResRoleKey record);

    int delByRoleId(@Param("roleId") Long roleId, @Param("userId") Long userId);

    int delByUserId(Long userId);

    int insertBatch(List<BackResRoleKey> list);

    List<BackResRoleKey> selectByRoleId(@Param("roleId") Long roleId, @Param("userId") Long userId);

    List<BackUser> selectByRes(Long resId);

    List<BackUser> selectByResIds(String resIds);
}