package com.device.dao;

import com.device.api.entity.Page;
import com.device.api.entity.backrole.BackUserLogin;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BackUserLoginMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BackUserLogin record);

    int insertSelective(BackUserLogin record);

    BackUserLogin selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BackUserLogin record);

    int updateByPrimaryKey(BackUserLogin record);

    List<BackUserLogin> selectPage(Page page);

    List<BackUserLogin> selectLoginInfo(Map<String, Object> map);

    int deleteByUid(@Param("userId") long userId);
}