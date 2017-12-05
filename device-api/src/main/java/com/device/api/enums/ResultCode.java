package com.device.api.enums;

/**
 * Created by  on 7/20/16.
 */
public enum ResultCode {
    OK(200, "成功"),
    FAIL(404, "失败"),
    ERROR(500, "服务器异常!"),
    BAD_REQUEST(400, "参数错误!"),
    SERVER_INTERNAL_ERROR(20000, "服务器内部错误！"),
    USER_MESSAGE_ERROR(20001, "用户名或者密码不正确"),
    ACCOUNT_LOCK(20002, "账户锁定"),
    ERROR_TIME_MORE(20003, "用户名或者密码错误次数过多"),
    NAME_REPETITION(20004, "名称重复")
    ;

    private int code;

    private String desc;

    ResultCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getErrorDesc(int code) {
        for (ResultCode rc : ResultCode.values()) {
            if (rc.getCode() == code) {
                return rc.getDesc();
            }
        }
        return "";
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
