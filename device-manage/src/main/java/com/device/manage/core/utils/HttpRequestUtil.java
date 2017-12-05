package com.device.manage.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Enumeration;

/**
 * Created by liuyuanzhou on 4/29/16.
 */
public class HttpRequestUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);

    private static final String ACCESS_TOKEN = "yt_token";

    private static final String EXCEPTION_ATT_NAME = "THROWABLE";

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

    public static String getErrorMessageForLogging(HttpServletRequest request) {
        Enumeration<String> enu = request.getParameterNames();
        StringBuilder sb = new StringBuilder();
        while (enu.hasMoreElements()) {
            String key = enu.nextElement();
            sb.append(key).append("=").append(request.getParameter(key)).append(",");
        }
        return String.format("logException path[%s] params[%s] fail.", request.getServletPath(),
                (sb.length() > 0 ? sb.subSequence(0, sb.length() - 1) : ""));
    }

    public static String getAccessToken(HttpServletRequest request) {
        // 优先读 cookie
        String accessToken = getCookie(request, ACCESS_TOKEN);
        if (StringUtils.isEmpty(accessToken) || "null".equals(accessToken)) {
            // 兼容 url 参数
            accessToken = request.getParameter(ACCESS_TOKEN);
        }
        return accessToken;
    }

    public static final String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (key.equalsIgnoreCase(cookies[i].getName())) {
                    try {
                        return URLDecoder.decode(cookies[i].getValue(), "UTF-8");
                    } catch (Exception e) {
                    }
                }
            }
        }
        return "";
    }

    public static final void setException(HttpServletRequest request, Throwable t) {
        request.setAttribute(EXCEPTION_ATT_NAME, t);
    }

    public static final Throwable getException(HttpServletRequest request) {
        return (Throwable) request.getAttribute(EXCEPTION_ATT_NAME);
    }

    private HttpRequestUtil() {
    }
}
