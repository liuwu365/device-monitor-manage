package com.device.manage.web.controller;

import com.device.api.entity.Result;
import com.device.api.entity.UserOnline;
import com.device.manage.web.controller.base.BaseController;
import com.device.manage.web.service.SessionService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Description: 在线用户控制器
 * @Date: 2018-03-22 10:59
 */
@Controller
@RequestMapping("/userOnline")
public class UserOnlineController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UserOnlineController.class);
    private static final Gson gson = new Gson();

    @Autowired
    SessionService sessionService;

    @ResponseBody
    @RequestMapping("/session/list")
    public Result list() {
        List<UserOnline> list = sessionService.list();
        return Result.success(list);
    }

    @ResponseBody
    @RequestMapping("/session/forceLogout")
    public Result forceLogout(String id) {
        try {
            sessionService.forceLogout(id);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("踢出用户失败");
        }
    }

}
