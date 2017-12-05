package com.device.manage.web.service.device;

import com.device.api.entity.Page;
import com.device.api.entity.device.DeviceWarnRule;
import com.device.api.uitls.CheckUtil;
import com.device.dao.DeviceWarnRuleMapper;
import com.device.manage.web.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @Date: 2017-12-02 15:42
 */
@Service
public class WarnRuleService implements BaseService<DeviceWarnRule> {
    @Resource
    private DeviceWarnRuleMapper deviceWarnRuleMapper;

    /**
     * 查询报警规则列表分页数据
     *
     * @param page
     * @return
     */
    @Override
    public Page<DeviceWarnRule> findPage(Page<DeviceWarnRule> page) {
        long total = deviceWarnRuleMapper.selectCountByPage(page);
        List<DeviceWarnRule> list = deviceWarnRuleMapper.selectByPage(page);
        page.setTotal(CheckUtil.isEmpty(total) ? 0 : total);
        page.setResult(list);
        return page;
    }

    public DeviceWarnRule selectByItemAndLevel(String item, int level) {
        return deviceWarnRuleMapper.selectByItemAndLevel(item, level);
    }

    @Override
    public List<DeviceWarnRule> selectAll() {
        return null;
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return deviceWarnRuleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(DeviceWarnRule obj) {
        return deviceWarnRuleMapper.insert(obj);
    }

    @Override
    public int insertSelective(DeviceWarnRule obj) {
        return deviceWarnRuleMapper.insertSelective(obj);
    }

    @Override
    public DeviceWarnRule selectByPrimaryKey(Long id) {
        return deviceWarnRuleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(DeviceWarnRule obj) {
        return deviceWarnRuleMapper.updateByPrimaryKeySelective(obj);
    }

    @Override
    public int updateByPrimaryKey(DeviceWarnRule obj) {
        return deviceWarnRuleMapper.updateByPrimaryKey(obj);
    }
}
