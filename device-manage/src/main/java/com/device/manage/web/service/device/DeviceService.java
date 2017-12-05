package com.device.manage.web.service.device;

import com.device.api.entity.Page;
import com.device.api.entity.device.DeviceInfo;
import com.device.api.uitls.CheckUtil;
import com.device.dao.DeviceInfoMapper;
import com.device.manage.web.service.BaseService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @Date: 2017-12-02 12:24
 */
@Service
public class DeviceService implements BaseService<DeviceInfo> {
    private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);
    private static final Gson gson = new Gson();

    @Resource
    private DeviceInfoMapper deviceInfoMapper;

    /**
     * 查询设备列表分页数据
     *
     * @param page
     * @return
     */
    @Override
    public Page<DeviceInfo> findPage(Page<DeviceInfo> page) {
        long total = deviceInfoMapper.selectCountByPage(page);
        List<DeviceInfo> list = deviceInfoMapper.selectByPage(page);
        page.setTotal(CheckUtil.isEmpty(total) ? 0 : total);
        page.setResult(list);
        return page;
    }

    /**
     * 根据Uid查询设备信息
     *
     * @param deviceUid
     * @return
     */
    public DeviceInfo findByDeviceUid(String deviceUid) {
        return deviceInfoMapper.selectByDeviceUid(deviceUid);
    }

    @Override
    public List<DeviceInfo> selectAll() {
        return deviceInfoMapper.selectAll();
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return deviceInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(DeviceInfo obj) {
        return deviceInfoMapper.insert(obj);
    }

    @Override
    public int insertSelective(DeviceInfo obj) {
        return deviceInfoMapper.insertSelective(obj);
    }

    @Override
    public DeviceInfo selectByPrimaryKey(Long id) {
        return deviceInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(DeviceInfo obj) {
        return deviceInfoMapper.updateByPrimaryKeySelective(obj);
    }

    @Override
    public int updateByPrimaryKey(DeviceInfo obj) {
        return deviceInfoMapper.updateByPrimaryKey(obj);
    }
}
