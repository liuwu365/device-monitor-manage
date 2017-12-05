package com.device.dao;

import com.device.api.entity.TreeObject;
import com.device.api.entity.backrole.BackResources;
import com.device.api.entity.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BackResourcesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BackResources record);

    int insertSelective(BackResources record);

    BackResources selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BackResources record);

    int updateByPrimaryKey(BackResources record);


    List<TreeObject> selectList();

    List<BackResources> selectPage(Page page);

    List<TreeObject> selectUserList(Long userId);

    List<BackResources> selectMenu(@Param("parentId") Long parentId);

    BackResources selectByName(String name);
}