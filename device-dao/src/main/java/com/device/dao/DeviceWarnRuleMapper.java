package com.device.dao;

import com.device.api.entity.Page;
import com.device.api.entity.device.DeviceWarnRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceWarnRuleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceWarnRule record);

    int insertSelective(DeviceWarnRule record);

    DeviceWarnRule selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceWarnRule record);

    int updateByPrimaryKey(DeviceWarnRule record);

    /**
     * 分页查询信息
     *
     * @param page
     * @return
     */
    List<DeviceWarnRule> selectByPage(Page<DeviceWarnRule> page);

    long selectCountByPage(Page<DeviceWarnRule> page);

    DeviceWarnRule selectByItemAndLevel(@Param("item") String item, @Param("level") int level);
}