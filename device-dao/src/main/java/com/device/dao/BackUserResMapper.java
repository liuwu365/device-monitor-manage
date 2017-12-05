package com.device.dao;

import com.device.api.entity.backrole.BackUserResKey;

import java.util.List;

public interface BackUserResMapper {
    int deleteByPrimaryKey(BackUserResKey key);

    int insert(BackUserResKey record);

    int insertSelective(BackUserResKey record);

    List<BackUserResKey> selectByUserId(Long userId);
}