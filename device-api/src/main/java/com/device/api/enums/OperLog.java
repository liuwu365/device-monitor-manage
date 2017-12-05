package com.device.api.enums;

/**
 * @Description: 操作日志类型
 * @date: 2017-07-04 17:09:53
 */
public enum OperLog {
    BACK_USER(1, "后台用户日志"),
    RESOURCE(2, "资源日志"),
    ;

    private int code;
    private String desc;

    OperLog(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
