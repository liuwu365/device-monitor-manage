package com.device.manage.core.utils;

import com.device.api.entity.backrole.BackUser;
import com.device.api.uitls.CheckUtil;
import com.device.manage.core.constant.Constant;
import com.device.manage.core.constant.BasicConstant;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author Created by Administrator on 2017/6/26.
 */
public class SessionUtil {
    private static String SESSION_USER = Constant.SESSION_USER;
    public static BackUser getCurrentUser() {
        BackUser backUser = (BackUser) getSession().getAttribute(BasicConstant.BACK_USER_INFO);
        return CheckUtil.isEmpty(backUser) ? new BackUser() : backUser;
    }

    public static Long getUserId() {
        return getCurrentUser().getId();
    }


    public static String getSessionId() {
        return SecurityUtils.getSubject().getSession().getId().toString();
    }

    public static void updateBackUser(BackUser backUser) {
        if (Objects.isNull(backUser)) {
            return;
        }
        getSession().setAttribute(BasicConstant.BACK_USER_INFO, backUser);
    }


    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    /**
     * 从session中获取用户信息
     *
     * @param request
     * @return SysUser
     */
    public static BackUser getUser(HttpServletRequest request) {
        return (BackUser) request.getSession(true).getAttribute(SESSION_USER);
    }
}
