package com.device.manage.web.controller.base.permisssion;

import com.device.dao.BackIpLimitMapper;
import com.google.gson.Gson;
import com.device.manage.web.controller.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * User: zc
 * Date: 2017/11/27.
 */
@Controller
@RequestMapping("/back/limit")
public class BackLimitController extends BaseController {

    private static final Gson gson = new Gson();

    @Resource
    private BackIpLimitMapper backIpLimitMapper;

    @RequestMapping(value = "/ip")
    @ResponseBody
    public String limitIp() {
        return null;
    }

}
