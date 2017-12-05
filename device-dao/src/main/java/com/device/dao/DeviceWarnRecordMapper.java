package com.device.dao;

import com.device.api.entity.Page;
import com.device.api.entity.device.DeviceWarnRecord;

import java.util.List;

public interface DeviceWarnRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceWarnRecord record);

    int insertSelective(DeviceWarnRecord record);

    DeviceWarnRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceWarnRecord record);

    int updateByPrimaryKey(DeviceWarnRecord record);

    /**
     * 分页查询信息(只查询存在报警设备的报警列表,即：等待处理的)
     *
     * @param page
     * @return
     */
    List<DeviceWarnRecord> selectByPage(Page<DeviceWarnRecord> page);

    long selectCountByPage(Page<DeviceWarnRecord> page);


}