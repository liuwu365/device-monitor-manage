package com.device.manage.core.utils;

import com.device.manage.core.constant.Constant;
import com.google.gson.Gson;
import com.device.api.uitls.CheckUtil;
import com.device.api.uitls.ErrorWriterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/19.
 *
 * @description Cookie 工具类
 */
public class CookieUtil {

    private static final Logger logger = LoggerFactory.getLogger(CookieUtil.class);

    static final Gson gson = new Gson();

    /**
     * 将cookie封装到Map里面
     *
     * @param request
     * @return
     */

    public static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

    /**
     * 获取登录时的tokean
     *
     * @param request
     * @return
     */
    public static Cookie getTokenByLogin(HttpServletRequest request) {
        Cookie cookie = readCookieMap(request).get(Constant.REMEMBERME_TOKEN);
        return cookie;
    }


    public static void setTokenCookie(String tokenCookie, HttpServletResponse response, boolean isRememberMe) {
        Cookie cookie = new Cookie(Constant.REMEMBERME_TOKEN, tokenCookie);
        if (isRememberMe) {
            logger.info("rememberMe is true");
            int remembermeDay = CheckUtil.isEmpty(Constant.REMEMBERME_DAY) ? 30 : Constant.REMEMBERME_DAY;
            cookie.setMaxAge(remembermeDay * 24 * 60 * 60);
        } else {
            logger.info("rememberMe is false");
        }
        cookie.setPath("/");
//        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static void setCookie(String key, String value, int maxAge, HttpServletResponse response) {
        if (null != value && CommonUtil.isContainsChinese(value)) {
            try {
                value = URLEncoder.encode(value, Constant.UTF8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
//        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static Cookie getCookie(String key, HttpServletRequest request) {
        Cookie cookie = readCookieMap(request).get(key);
        if (null != cookie) {
            try {
                URLDecoder.decode(key, Constant.UTF8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return cookie;
    }

    public static void removeCookie(String key, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        try {
            if (!CheckUtil.isEmpty(cookies)) {
                for (int i = 0; i < cookies.length; i++) {
                    Cookie cookie = new Cookie(cookies[i].getName(), null);
                    if (cookies[i].getName().equals(key)) {
                        cookie.setValue("");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("remove Cookies ERROR={}", ErrorWriterUtil.writeError(ex).toString());
        }
    }

    public static void removeToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = readCookieMap(request).get(Constant.REMEMBERME_TOKEN);
        logger.info("removeToken cookie={}", gson.toJson(cookie));
        if (!CheckUtil.isEmpty(cookie)) {
            cookie.setPath("/");
            cookie.setValue(null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            logger.info("del ={}", gson.toJson(getTokenByLogin(request)));
        }
    }

    public static void removeUserAll(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        try {
            if (!CheckUtil.isEmpty(cookies)) {
                for (int i = 0; i < cookies.length; i++) {
                    Cookie cookie = new Cookie(cookies[i].getName(), null);
                    logger.info("cookie [{}]|cookie={}", i, gson.toJson(cookie));
                    if (null != cookie && !cookies[i].getName().equals("JSESSIONID")) {
                        cookie.setMaxAge(0);
                        cookie.setPath("/");//根据你创建cookie的路径进行填写
                        response.addCookie(cookie);
                    } else {
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("removeUserAll Cookies ERROR={}", ErrorWriterUtil.writeError(ex).toString());
        }
    }

    public static void removeAll(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        try {
            if (!CheckUtil.isEmpty(cookies)) {
                for (int i = 0; i < cookies.length; i++) {
                    //System.out.println(cookies[i].getName() + ”:” + cookies[i].getValue());
                    Cookie cookie = new Cookie(cookies[i].getName(), null);
                    logger.info("cookie [{}]|cookie={}", i, gson.toJson(cookie));
                    if (null != cookie) {
                        cookie.setMaxAge(0);
                        cookie.setPath("/");//根据你创建cookie的路径进行填写
                        response.addCookie(cookie);
                    } else {
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("clearAll Cookies ERROR={}", ErrorWriterUtil.writeError(ex).toString());
        }
    }
}
