package com.device.dao;

import com.device.api.entity.Page;
import com.device.api.entity.device.DeviceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceInfo record);

    int insertSelective(DeviceInfo record);

    DeviceInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceInfo record);

    int updateByPrimaryKey(DeviceInfo record);

    /**
     * 分页查询信息
     *
     * @param page
     * @return
     */
    List<DeviceInfo> selectByPage(Page<DeviceInfo> page);

    long selectCountByPage(Page<DeviceInfo> page);

    List<DeviceInfo> selectAll();

    DeviceInfo selectByDeviceUid(@Param("deviceUid") String deviceUid);

}