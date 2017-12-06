package com.device.manage.web.service.user;

import com.device.api.entity.Page;
import com.device.api.entity.device.UserInfo;
import com.device.api.uitls.CheckUtil;
import com.device.dao.UserInfoMapper;
import com.device.manage.web.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @Date: 2017-12-05 14:53
 */
@Service
public class UserService implements BaseService<UserInfo> {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public Page<UserInfo> findPage(Page<UserInfo> page) {
        long total = userInfoMapper.selectCountByPage(page);
        List<UserInfo> list = userInfoMapper.selectByPage(page);
        page.setTotal(CheckUtil.isEmpty(total) ? 0 : total);
        page.setResult(list);
        return page;
    }

    public UserInfo selectByAccount(String account) {
        return userInfoMapper.selectByAccount(account);
    }

    public UserInfo selectByRealName(String realName) {
        return userInfoMapper.selectByRealName(realName);
    }

    @Override
    public List<UserInfo> selectAll() {
        return null;
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return userInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserInfo obj) {
        return userInfoMapper.insert(obj);
    }

    @Override
    public int insertSelective(UserInfo obj) {
        return userInfoMapper.insertSelective(obj);
    }

    @Override
    public UserInfo selectByPrimaryKey(Long id) {
        return userInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserInfo obj) {
        return userInfoMapper.updateByPrimaryKeySelective(obj);
    }

    @Override
    public int updateByPrimaryKey(UserInfo obj) {
        return userInfoMapper.updateByPrimaryKey(obj);
    }

}
