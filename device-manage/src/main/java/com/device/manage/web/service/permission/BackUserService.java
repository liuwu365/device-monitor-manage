package com.device.manage.web.service.permission;

import com.device.api.entity.Page;
import com.device.api.entity.Result;
import com.device.api.entity.backrole.BackUser;
import com.device.api.entity.backrole.BackUserLogin;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface BackUserService {

    /**
     * 添加或修改用户
     *
     * @param user
     * @return
     */
    Result addOrUpdateItem(BackUser user);

    /**
     * 获取用户信息
     *
     * @param sId
     * @return
     */
    BackUser getItem(Long sId);

    BackUser getItemByAccountName(String accountName);

    /**
     * 获取用户列表
     *
     * @return
     */
    Page<BackUser> getPage(Page page);


    /**
     * 获取用户登录列表
     *
     * @return
     */
    Page<BackUserLogin> getLoginPage(Page page);

    /**
     * 用户登录日志导出到Excle
     *
     * @param map
     * @return
     */
    List<BackUserLogin> getLoginInfoList(Map<String, Object> map);

    /**
     * 新增用户登录记录
     *
     * @return
     */
    Result addUserLoginRecord(HttpServletRequest request);

    /**
     * 删除一项用户
     *
     * @param id
     * @return
     */
    Result delItem(Long id);


    /**
     * 账号是否存在
     *
     * @param accountName
     * @return 存在：true
     */
    Result isExisit(String accountName);

    /**
     * 角色用户关联
     *
     * @param roles
     * @param userId
     * @return
     */
    Result saveUserRoleRelation(String roles, Long userId);

    /**
     * 获取角色用户关联
     *
     * @param userId
     * @return
     */
    Result getUserRoleList(Long userId);

    /**
     * 修改密码
     *
     * @param password
     * @param nPassword
     * @return
     */
    Result changePassword(BackUser user, String password, String nPassword);

    /**
     * 重置密码（123456）
     *
     * @param userId
     * @return
     */
    Result resetPassword(Long userId);

    boolean pathsMatch(String path, ServletRequest request);

}
