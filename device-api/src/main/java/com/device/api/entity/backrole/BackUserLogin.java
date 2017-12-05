package com.device.api.entity.backrole;

import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.util.Date;

public class BackUserLogin implements Serializable {
    @Excel(name = "序号")
    private Long id;
    @Excel(name = "用户Id")
    private Long userId;
    private int type;
    @Excel(name = "登录类型")
    private String strType;
    @Excel(name = "帐号")
    private String accountName;
    @Excel(name = "登录时间", exportFormat = "yyyy-MM-dd HH:mm:ss", width = 25)
    private Date loginTime;
    @Excel(name = "登录Ip",width = 30)
    private String loginIp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp == null ? null : loginIp.trim();
    }

    public String getStrType() {
        return strType;
    }

    public void setStrType(String strType) {
        this.strType = strType;
    }
}