package com.device.manage.web.controller.base.permisssion;

import com.device.api.entity.Page;
import com.device.api.entity.Result;
import com.device.api.entity.backrole.BackIpLimit;
import com.device.api.entity.backrole.BackUser;
import com.device.api.uitls.CheckUtil;
import com.device.api.uitls.ErrorWriterUtil;
import com.device.manage.web.service.permission.BackIpLimitService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.device.manage.core.utils.SessionUtil.getCurrentUser;


/**
 * Created by Administrator on 2017/10/13.
 */
@Controller
@RequestMapping("/base/power")
public class BackIpLimitController {

    private static final Logger logger = LoggerFactory.getLogger(BackIpLimitController.class);
    private static final Gson gson = new Gson();

    @Resource
    private BackIpLimitService backIpLimitService;

    @RequestMapping(value = "/list")
    public String list(Model model, Page<BackIpLimit> page) {
        try {
            page = this.backIpLimitService.getPage(page);
            model.addAttribute("page", page);
        } catch (Exception e) {
            logger.error("backUser/list error|page={}|ex={}", gson.toJson(page), ErrorWriterUtil.writeError(e).toString());
        }
        return "base/power/list";
    }

    //跳转到新增页面
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String iPAddressAdd() {
        return "base/power/add";
    }


    //新增Ip操作
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result addIpLimit(HttpServletRequest request, BackIpLimit backIpLimit, String ipAddress) {
        Result result;
        BackUser backUser=null;
        try {
            backUser = getCurrentUser();
            if (CheckUtil.isEmpty(backIpLimit.getIpAddress())) {
                return Result.failure("IP不能为空");
            }
            if (backIpLimitService.isExisit(backIpLimit.getIpAddress())) {
                return Result.failure("该IP已存在");
            }
            //添加IP
            backIpLimit.setAddPeople(backUser.getUserName());
            backIpLimitService.insertSelective(backIpLimit);
            result = Result.success();
        } catch (Exception e) {
            logger.error("add ipAddress error|ex={}", ErrorWriterUtil.writeError(e));
            result = Result.failure("服务器异常");
        }
        return result;
    }

    @RequestMapping(value = "/deleteIpAddress")
    @ResponseBody
    public Result deleteGroup(HttpServletRequest request, @RequestParam Long id) {
        Result result;
        try {
            result = backIpLimitService.deleteByPrimaryKey(id) == 1 ? Result.success() : Result.failure();
        } catch (Exception e) {
            logger.error("backDept/addDeptGroup error|ex={}", ErrorWriterUtil.writeError(e));
            result = Result.failure("服务器异常");
        }
        return result;
    }
}
