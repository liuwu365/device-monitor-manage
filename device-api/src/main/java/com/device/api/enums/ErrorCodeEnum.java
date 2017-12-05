package com.device.api.enums;

/**
 * Created by  on 4/19/16.
 */
public enum ErrorCodeEnum {
    OK(200),
    INVALID_ARGS(400),
    SERVER_INTERNAL_ERROR(500),
    INVALID_TOKEN(1000001),
    TOKEN_EXPIRED(1000002),
    NOT_CHANGED(1000003);

    ErrorCodeEnum(int code) {
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
