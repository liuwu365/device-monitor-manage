package com.device.dao;


import com.device.api.entity.backrole.BackUserRoleKey;

import java.util.List;

public interface BackUserRoleMapper {
    int deleteByPrimaryKey(BackUserRoleKey key);

    int insert(BackUserRoleKey record);

    int insertSelective(BackUserRoleKey record);

    int delByUserId(Long userId);

    int insertBatch(List<BackUserRoleKey> list);

    List<BackUserRoleKey> selectByUserId(Long userId);
}