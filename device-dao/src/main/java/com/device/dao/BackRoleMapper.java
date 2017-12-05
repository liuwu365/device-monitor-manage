package com.device.dao;


import com.device.api.entity.Page;
import com.device.api.entity.backrole.BackRole;

import java.util.List;

public interface BackRoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BackRole record);

    int insertSelective(BackRole record);

    BackRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BackRole record);

    int updateByPrimaryKey(BackRole record);

    List<BackRole> selectPage(Page page);

    List<BackRole> selectList();

    /**
     * 查询角色条数
     *
     * @return
     */
    int selectCount();

    /**
     * 包含id的新增
     * @param record
     * @return
     */
    int insertContainId(BackRole record);

    BackRole selectByName(String name);
}