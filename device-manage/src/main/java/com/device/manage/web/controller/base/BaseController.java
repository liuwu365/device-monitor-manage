package com.device.manage.web.controller.base;


import com.device.api.entity.backrole.BackUser;
import com.device.manage.core.utils.CommonUtil;
import com.device.manage.core.utils.ExcelOperateUtil;
import com.device.manage.core.utils.SessionUtil;
import com.device.api.entity.Result;
import com.device.api.enums.ResultCode;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
public class BaseController {
	public static Logger LOG = Logger.getLogger(BaseController.class);

	public static final Gson gson = new Gson();

	public final String application_json = "application/json;charset=UTF-8";

	@InitBinder
	protected void init(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		CustomDateEditor dateEditor = new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}

	/**
	 * 获取当前登录用户
	 *
	 * @return
	 */
	public static BackUser getCurrentUser() {
		return SessionUtil.getCurrentUser();
	}

	public static String getCurrentAccountName() {
		return SecurityUtils.getSubject().getPrincipal().toString();
	}

	/**
	 * java判断是否拥有该权限
	 */
	public static void isAuthoriza(String authorizaCode) {
		//权限校验。判断是否包含权限。
		Subject subject = SecurityUtils.getSubject();
		//具体响应ShiroDbRealm。doGetAuthorizationInfo，判断是否包含此url的响应权限
		boolean isPermitted = subject.isPermitted(authorizaCode);
		if (isPermitted) {
			System.out.println("拥有该权限！");
		} else {
			System.out.println("没有该权限！");
			throw new AuthorizationException();
		}
	}

	/**
	 * 导出到指定路径下
	 * list:数据集合
	 * path：导出的路径
	 * strExcTitle:导出Excel列头名称
	 */
	public static void exportMethod2(List<?> list, String path, String[] strExcTitle, HttpServletResponse response) throws Exception {
		try {
			if (list.size() > 0) {
				//当前时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒");
				String time = sdf.format(new Date());
				//生成当天文件夹
				String nowFileName = CommonUtil.createFile(path);
				String fileName = path + nowFileName + "/" + getCurrentUser().getAccountName() + "_" + time + ".xls"; //格式：登录者账号_年月日时分秒
				OutputStream out = new FileOutputStream(fileName);

				//标题参数
				String title = "sheet1";
				//时间格式参数
				String fomatStr = "yyyy-MM-dd hh:mm:ss";
				//调用
				ExcelOperateUtil.exportExcel(title, strExcTitle, list, out, fomatStr);
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	/**
	 * 以弹框形式，用户可指定自己的路径
	 * 导出到指定路径下
	 * list:数据集合
	 * path：导出的路径
	 * strExcTitle:导出Excel列头名称
	 */
	public static void exportMethod(List<?> list, String[] strExcTitle, HttpServletResponse response) throws Exception {
		try {
			//当前时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH点mm分ss秒");
			String time = sdf.format(new Date());
			String fileName = time + ".xls"; //格式：登录者账号_年月日时分秒
			response.setContentType("application/x-msdownload");
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("GBK"), "iso8859-1"));
			OutputStream out = response.getOutputStream();
			//标题参数
			String title = "sheet1";
			//时间格式参数
			String fomatStr = "yyyy-MM-dd HH:mm:ss";
			//调用
			ExcelOperateUtil.exportExcel(title, strExcTitle, list, out, fomatStr);
			out.flush();
			out.close();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public Result getBadRequestResult(Result result) {
		result.setCode(ResultCode.BAD_REQUEST.getCode());
		result.setMsg(ResultCode.BAD_REQUEST.getDesc());
		return result;
	}

	public Result getServerInternalErrorResult(Result result) {
		result.setCode(ResultCode.SERVER_INTERNAL_ERROR.getCode());
		result.setMsg(ResultCode.SERVER_INTERNAL_ERROR.getDesc());
		return result;
	}

	public static final String getRealIP(HttpServletRequest req) {
		String ip = getFirstNonBlankHeader(req, "X-Real-IP", "x-real-ip", "X-Forwarded-For", "x-forwarded-for");
		if (ip == null) {
			ip = req.getRemoteAddr();
			if (ip == null) {
				ip = "";
			}
		}
		int idx = ip.indexOf(",");
		return (idx != -1) ? ip.substring(0, idx).trim() : ip;
	}

	public static String getFirstNonBlankHeader(HttpServletRequest req, String... headerNames) {
		if (req == null) {
			return null;
		}
		for (String name : headerNames) {
			String value = req.getHeader(name);
			if (StringUtils.isNotBlank(value)) {
				return value;
			}
		}
		return null;
	}
}
