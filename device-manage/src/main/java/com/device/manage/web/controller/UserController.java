package com.device.manage.web.controller;

import com.device.api.entity.Page;
import com.device.api.entity.Result;
import com.device.api.entity.device.UserInfo;
import com.device.api.uitls.CheckUtil;
import com.device.api.uitls.ErrorWriterUtil;
import com.device.manage.core.utils.SecurityUtil;
import com.device.manage.web.service.user.UserService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Description:
 * @Date: 2017-12-05 14:47
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final Gson gson = new Gson();

    @Resource
    private UserService userService;

    private final String listPage = "model/user/user_list";
    private final String addPage = "model/user/user_add";
    private final String editPage = "model/user/user_edit";

    @RequestMapping(value = "/user_list")
    public String userList(Model model, Page<UserInfo> page) {
        try {
            page = userService.findPage(page);
            model.addAttribute("page", page);
            logger.info("select user_list={}", gson.toJson(page));
        } catch (Exception e) {
            logger.error("user_list error|page={}|ex={}", gson.toJson(page), ErrorWriterUtil.writeError(e));
        }
        return listPage;
    }

    @RequestMapping(value = "/user_add", method = RequestMethod.GET)
    public String userAdd() {
        return addPage;
    }

    @RequestMapping(value = "/user_add", method = RequestMethod.POST)
    @ResponseBody
    public Result userAdd(HttpServletRequest request, UserInfo userInfo) {
        Result meta = null;
        try {
            userInfo.setPassword(SecurityUtil.MD5("123456"));
            UserInfo info = userService.selectByAccount(userInfo.getAccount());
            if (!CheckUtil.isEmpty(info)) {
                return Result.failure("用户帐号已存在");
            }
            UserInfo info2 = userService.selectByRealName(userInfo.getRealName());
            if (!CheckUtil.isEmpty(info2)) {
                return Result.failure("用户真实姓名已存在");
            }
            userInfo.setUpdateTime(new Date());
            int count = userService.insertSelective(userInfo);
            meta = count > 0 ? Result.success() : Result.failure();
        } catch (Exception e) {
            logger.error("add user error|ex={}", ErrorWriterUtil.writeError(e).toString());
            meta = Result.error();
        }
        return meta;
    }

    @RequestMapping("/deleteUser/{id}")
    @ResponseBody
    public Result deleteUser(HttpServletRequest request, @PathVariable("id") Long id) {
        Result meta = null;
        try {
            int count = userService.deleteByPrimaryKey(id);
            meta = count > 0 ? Result.success() : Result.failure();
        } catch (Exception e) {
            logger.error("deleteUser id={}|error={}", id, ErrorWriterUtil.writeError(e).toString());
            meta = Result.error();
        }
        return meta;
    }

    @RequestMapping(value = "/loadUserInfo", method = RequestMethod.GET)
    public String loadUserInfo(Model model, @RequestParam(value = "userId", required = false) Long userId) throws Exception {
        UserInfo userInfo = userService.selectByPrimaryKey(userId);
        model.addAttribute("user", userInfo);
        return editPage;
    }

    @RequestMapping(value = "/editUser", method = RequestMethod.POST)
    @ResponseBody
    public Result editUser(HttpServletRequest request, UserInfo userInfo) {
        Result result;
        try {
            userInfo.setPassword(SecurityUtil.MD5(userInfo.getPassword()));
            userInfo.setUpdateTime(new Date());
            int count = userService.updateByPrimaryKeySelective(userInfo);
            result = count > 0 ? Result.success() : Result.failure();
        } catch (Exception e) {
            logger.error("edit user error|ex={}", ErrorWriterUtil.writeError(e).toString());
            result = Result.error();
        }
        return result;
    }

    //编辑用户操作(启用或禁用)
    @RequestMapping(value = "/editUserStatus", method = RequestMethod.POST)
    @ResponseBody
    public Result editUserStatus(HttpServletRequest request, long id, int status) {
        Result result;
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(id);
            userInfo.setStatus((short) status);
            userInfo.setUpdateTime(new Date());
            int count = userService.updateByPrimaryKeySelective(userInfo);
            result = count > 0 ? Result.success() : Result.failure();
        } catch (Exception e) {
            logger.error("edit user status error|ex={}", ErrorWriterUtil.writeError(e).toString());
            result = Result.error();
        }
        return result;
    }

}
