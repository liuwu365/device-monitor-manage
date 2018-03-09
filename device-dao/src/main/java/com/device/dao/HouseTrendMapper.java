package com.device.dao;

import com.device.api.entity.HouseTrend;

public interface HouseTrendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(HouseTrend record);

    int insertSelective(HouseTrend record);

    HouseTrend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HouseTrend record);

    int updateByPrimaryKey(HouseTrend record);
}