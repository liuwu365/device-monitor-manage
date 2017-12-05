package com.device.manage.web.service.permission;

import com.device.api.entity.Result;
import com.device.api.entity.backrole.BackRole;
import com.device.api.entity.backrole.BackUser;
import com.device.api.entity.backrole.BackUserRoleKey;
import com.device.api.uitls.CheckUtil;
import com.device.dao.BackRoleMapper;
import com.device.dao.BackUserRoleMapper;
import com.device.manage.core.constant.BasicConstant;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
public class BackInfoServiceImpl implements BackInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackInfoServiceImpl.class);
    private static final Gson GSON = new Gson();

    @Resource
    private BackRoleMapper backRoleMapper;
    @Resource
    private BackUserRoleMapper backUserRoleMapper;
    @Resource
    private BackUserService backUserService;

    @Override
    @Transactional
    public Result saveUserInfo(BackUser backUser, String roles) {
        try {
            //检测有没有为2的默认角色，没有添加一个默认Id为2的角色(1超级管理员拥有所有权限)
            BackRole backRole = new BackRole();
            int count = backRoleMapper.selectCount();
            if (count == 0) {
                backRole.setId(2L);
                backRole.setState(0);
                backRole.setName("默认");
                backRole.setRoleKey("default");
                backRole.setDescription("默认角色");
                backRoleMapper.insertContainId(backRole);
            }
            Result result = this.backUserService.addOrUpdateItem(backUser);
            //用户角色管理，用户没有关联角色，添加一个默认的角色
            if (CheckUtil.isEmpty(roles)) {
                BackUserRoleKey backUserRole = new BackUserRoleKey();
                backUserRole.setUserId(backUser.getId());
                backUserRole.setRoleId(CheckUtil.isEmpty(backRole.getId()) ? 2L : backRole.getId());
                backUserRoleMapper.insert(backUserRole);
            }
            //Result result1 = this.backUserService.saveUserRoleRelation(roles, backUser.getId());
            if (result.getCode() == 200) {
                return result;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result.failure(BasicConstant.SAVE_FAILED);
    }

}
