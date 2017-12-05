package com.device.manage.web.service.permission;

import com.device.api.entity.Page;
import com.device.api.entity.backrole.BackIpLimit;
import com.device.api.uitls.CheckUtil;
import com.device.dao.BackIpLimitMapper;
import com.device.manage.web.service.BaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/10/13.
 */
@Service
public class BackIpLimitServiceImpl implements BaseService<BackIpLimit>, BackIpLimitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackIpLimitServiceImpl.class);

    private static final Gson GSON = new Gson();

    @Resource
    private BackIpLimitMapper backIpLimitMapper;


    @Override
    public Page<BackIpLimit> getPage(Page page) {
        PageHelper.startPage((int) page.getPage(), page.getLimit());
        List<BackIpLimit> itemList = this.backIpLimitMapper.selectPage(page);
        PageInfo pageInfo = new PageInfo(itemList);
        page.setResult(pageInfo.getList());
        page.setTotal(pageInfo.getTotal());
        return page;
    }

    @Override
    public Page<BackIpLimit> findPage(Page<BackIpLimit> page) {
        return null;
    }

    @Override
    public List<BackIpLimit> selectAll() {
        return null;
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return backIpLimitMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(BackIpLimit obj) {
        return backIpLimitMapper.insert(obj);
    }

    @Override
    public int insertSelective(BackIpLimit obj) {
        return backIpLimitMapper.insertSelective(obj);
    }

    @Override
    public BackIpLimit selectByPrimaryKey(Long id) {
        return backIpLimitMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(BackIpLimit obj) {
        return backIpLimitMapper.updateByPrimaryKeySelective(obj);
    }

    @Override
    public int updateByPrimaryKey(BackIpLimit obj) {
        return backIpLimitMapper.updateByPrimaryKey(obj);
    }

    @Override
    public boolean isExisit(String ipAddress) {
        BackIpLimit backIpLimit = backIpLimitMapper.selectByIpAddress(ipAddress);
        return CheckUtil.isEmpty(backIpLimit) ? false : true;
    }
}
