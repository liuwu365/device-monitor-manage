package com.device.dao;

import com.device.api.entity.Page;
import com.device.api.entity.backrole.BackUser;

import java.util.List;

public interface BackUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BackUser record);

    int insertSelective(BackUser record);

    BackUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BackUser record);

    int updateByPrimaryKey(BackUser record);

    List<BackUser> selectPage(Page page);

    BackUser selectByAccountName(String accountName);

    BackUser selectByUserName(String userName);

}