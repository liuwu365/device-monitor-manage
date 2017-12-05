package com.device.manage.web.controller.base.permisssion;

import com.device.api.entity.Page;
import com.device.api.entity.Result;
import com.device.api.entity.backrole.BackRole;
import com.device.api.entity.backrole.BackUser;
import com.device.api.entity.backrole.BackUserLogin;
import com.device.api.enums.OperLog;
import com.device.api.uitls.CheckUtil;
import com.device.api.uitls.ErrorWriterUtil;
import com.device.manage.web.controller.base.BaseController;
import com.device.manage.web.service.log.OperationLogService;
import com.device.manage.web.service.permission.BackInfoService;
import com.device.manage.web.service.permission.BackRoleService;
import com.device.manage.web.service.permission.BackUserService;
import com.device.manage.web.service.permission.UserOperationService;
import com.google.gson.Gson;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
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
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/base/user")
public class BackUserController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BackUserController.class);
    private static final Gson gson = new Gson();

    @Resource
    private BackInfoService backInfoService;
    @Resource
    private BackUserService backUserService;
    @Resource
    private BackRoleService backRoleService;
    @Resource
    private OperationLogService operationLogService;
    @Resource
    private UserOperationService userOperationService;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model) {
        List<BackRole> list = this.backRoleService.getList();
        model.addAttribute("list", list);
        return "base/user/add";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Model model, @RequestParam Long id) {
        BackUser item = this.backUserService.getItem(id);
        model.addAttribute("backUser", item);
        List<BackRole> list = this.backRoleService.getList();
        model.addAttribute("list", list);
        return "base/user/edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(HttpServletRequest request, BackUser backUser, String roles) {
        logger.info("request /base/user/save backUser={}|roles={}|", gson.toJson(backUser), gson.toJson(roles));
        Result result = null;
        try {
            String tag = CheckUtil.isEmpty(backUser.getId()) ? "新增" : "修改";
            result = this.backInfoService.saveUserInfo(backUser, roles);
            operationLogService.addNormalOperLog(request, OperLog.BACK_USER, String.format("%s用户【%s】成功", tag, backUser.getUserName()));
        } catch (Exception e) {
            logger.error("backUser/save error|backUser={}|ex={}", gson.toJson(backUser), ErrorWriterUtil.writeError(e).toString());
            result = new Result(500, "服务器异常");
        }
        logger.info("request /base/role/save result={}", gson.toJson(result));
        return result;
    }

    @RequestMapping(value = "/list")
    public String list(Model model, Page<BackUser> page) {
        try {
            page = this.backUserService.getPage(page);
            model.addAttribute("page", page);
        } catch (Exception e) {
            logger.error("backUser/list error|page={}|ex={}", gson.toJson(page), ErrorWriterUtil.writeError(e).toString());
        }
        return "base/user/list";
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public Result delete(HttpServletRequest request, @RequestParam Long id, @RequestParam String userName) {
        logger.info("request /base/user/delete id={}", gson.toJson(id));
        Result result = null;
        try {
            operationLogService.addNormalOperLog(request, OperLog.BACK_USER, String.format("删除用户【%s】成功", userName));
            result = this.backUserService.delItem(id);
        } catch (Exception e) {
            logger.error("backUser/delete error|id={}|ex={}", id, ErrorWriterUtil.writeError(e).toString());
            result = new Result(500, "服务器异常");
        }
        logger.info("request /base/user/delete result={}", gson.toJson(result));
        return result;
    }

    @RequestMapping(value = "/isExist")
    @ResponseBody
    public boolean isExist(String accountName, @RequestParam(value = "flag", defaultValue = "false") boolean fg) {
        boolean flag = false;
        try {
            Result result = this.backUserService.isExisit(accountName);
            if (result.getCode() == 200) {
                if (!fg) {
                    flag = !(boolean) result.getT();
                } else {
                    flag = (boolean) result.getT();
                }
            }
        } catch (Exception e) {
            logger.error("backUser/isExist error|accountName={}|ex={}", accountName, ErrorWriterUtil.writeError(e).toString());
        }
        return flag;
    }

    @RequestMapping(value = "/roleList")
    @ResponseBody
    public Result roleList(Long userId) {
        Result result = null;
        try {
            result = this.backUserService.getUserRoleList(userId);
        } catch (Exception e) {
            logger.error("backUser/roleList error|userId={}|ex={}", userId, ErrorWriterUtil.writeError(e).toString());
            result = new Result(500, "服务器异常");
        }
        return result;
    }

    @RequestMapping(value = "/changePassword")
    @ResponseBody
    public Result changePassword(HttpServletRequest request, String password, String nPassword) {
        Result result = null;
        BackUser user = null;
        try {
            user = getCurrentUser();
            logger.info("request /base/user/changePassword user={}", gson.toJson(user));
            result = this.backUserService.changePassword(user, password, nPassword);
            operationLogService.addNormalOperLog(request, OperLog.BACK_USER, String.format("修改用户【%s】密码成功", user.getUserName()));
        } catch (Exception e) {
            logger.error("backUser/changePassword error|user={}|password={}|nPassword={}|ex={}", user, password, nPassword, ErrorWriterUtil.writeError(e).toString());
            result = new Result(500, "服务器异常");
        }
        logger.info("request /base/user/changePassword result={}", gson.toJson(result));
        return result;
    }

    @RequestMapping(value = "/login/list")
    public String loginList(Model model, Page<BackUserLogin> page) {
        try {
            page = this.backUserService.getLoginPage(page);
            model.addAttribute("page", page);
        } catch (Exception e) {
            logger.error("backUser/loginList error|page={}|ex={}", gson.toJson(page), ErrorWriterUtil.writeError(e).toString());
        }
        return "base/user/login_list";
    }

    @RequestMapping(value = "/loginLogToExcel")
    public void loginLogToExcel(String beginDate, String endDate, String accountName, HttpServletResponse response) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("beginDate", beginDate);
            map.put("endDate", endDate);
            map.put("accountName", accountName);
            List<BackUserLogin> userLoginList = backUserService.getLoginInfoList(map);
            ExportParams params = new ExportParams();
            params.setType(ExcelType.XSSF);
            Workbook workbook = ExcelExportUtil.exportExcel(params, BackUserLogin.class, userLoginList);
            //当前时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒");
            String time = sdf.format(new Date());
            String fileName = "登录日志记录(" + time + ").xls"; //格式：登录者账号_年月日时分秒
            //输出流
            response.setContentType("application/x-msdownload");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("GBK"), "iso8859-1"));
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("backUser/loginLogToExcel error|ex={}", ErrorWriterUtil.writeError(e).toString());
        }
    }

    @RequestMapping(value = "/resetPassword")
    @ResponseBody
    public Result resetPassword(HttpServletRequest request, Long userId) {
        Result result = null;
        try {
            logger.info("request /base/user/resetPassword userId={}", userId);
            result = this.backUserService.resetPassword(userId);
            BackUser user = backUserService.getItem(userId);
            operationLogService.addNormalOperLog(request, OperLog.BACK_USER, String.format("重置用户【%s】密码成功", user.getUserName()));
        } catch (Exception e) {
            logger.error("backUser/resetPassword error|userId={}|ex={}", userId, ErrorWriterUtil.writeError(e).toString());
            result = new Result(500, "服务器异常");
        }
        logger.info("request /base/user/resetPassword result={}", gson.toJson(result));
        return result;
    }

    @RequestMapping(value = "/lockUser")
    @ResponseBody
    public Result lockUser(HttpServletRequest request, @RequestParam Long id, @RequestParam Boolean lock, @RequestParam String accountName) {
        Result result;
        try {
            BackUser user = new BackUser();
            user.setId(id);
            user.setLocked(lock);
            result = backUserService.addOrUpdateItem(user);
            operationLogService.addNormalOperLog(request, OperLog.BACK_USER, String.format("锁定用户账号【%s】", accountName));
        } catch (Exception e) {
            logger.error("backUser/lockUser error|ex={}", ErrorWriterUtil.writeError(e).toString());
            result = new Result(500, "服务器异常");
        }
        return result;
    }

    @RequestMapping(value = "/operationList")
    public String operationList(Model model, @RequestParam(value = "targetUid", required = false) Long targetUid,
                                Page page) throws Exception {
        page.getFilter().put("targetUid", targetUid);
        page = userOperationService.findPage(page);
        model.addAttribute("page", page);
        logger.info("operationList result={}", gson.toJson(page));
        return "base/user/operation_list";
    }



}
