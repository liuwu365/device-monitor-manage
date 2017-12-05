package com.device.dao;

import com.device.api.entity.Page;
import com.device.api.entity.backrole.BackIpLimit;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BackIpLimitMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BackIpLimit record);

    int insertSelective(BackIpLimit record);

    BackIpLimit selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BackIpLimit record);

    int updateByPrimaryKey(BackIpLimit record);

    List<BackIpLimit> selectPage(Page page);

    BackIpLimit selectByIpAddress(@Param("ipAddress") String ipAddress);
}