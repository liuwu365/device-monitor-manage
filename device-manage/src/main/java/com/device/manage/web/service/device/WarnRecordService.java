package com.device.manage.web.service.device;

import com.device.api.entity.Page;
import com.device.api.entity.device.DeviceWarnRecord;
import com.device.api.uitls.CheckUtil;
import com.device.dao.DeviceWarnRecordMapper;
import com.device.manage.web.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @Date: 2017-12-02 17:21
 */
@Service
public class WarnRecordService implements BaseService<DeviceWarnRecord> {
    @Resource
    private DeviceWarnRecordMapper deviceWarnRecordMapper;

    @Override
    public Page<DeviceWarnRecord> findPage(Page<DeviceWarnRecord> page) {
        long total = deviceWarnRecordMapper.selectCountByPage(page);
        List<DeviceWarnRecord> list = deviceWarnRecordMapper.selectByPage(page);
        page.setTotal(CheckUtil.isEmpty(total) ? 0 : total);
        page.setResult(list);
        return page;
    }

    @Override
    public List<DeviceWarnRecord> selectAll() {
        return null;
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return deviceWarnRecordMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(DeviceWarnRecord obj) {
        return deviceWarnRecordMapper.insert(obj);
    }

    @Override
    public int insertSelective(DeviceWarnRecord obj) {
        return deviceWarnRecordMapper.insertSelective(obj);
    }

    @Override
    public DeviceWarnRecord selectByPrimaryKey(Long id) {
        return deviceWarnRecordMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(DeviceWarnRecord obj) {
        return deviceWarnRecordMapper.updateByPrimaryKeySelective(obj);
    }

    @Override
    public int updateByPrimaryKey(DeviceWarnRecord obj) {
        return deviceWarnRecordMapper.updateByPrimaryKey(obj);
    }
}
