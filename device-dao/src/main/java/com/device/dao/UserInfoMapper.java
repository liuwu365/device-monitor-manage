package com.device.dao;

import com.device.api.entity.Page;
import com.device.api.entity.device.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    /**
     * 分页查询信息
     *
     * @param page
     * @return
     */
    List<UserInfo> selectByPage(Page<UserInfo> page);

    long selectCountByPage(Page<UserInfo> page);

    UserInfo selectByAccount(@Param("account") String account);

    UserInfo selectByRealName(@Param("realName") String realName);


}