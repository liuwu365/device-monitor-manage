package com.device.api.enums;

/**
 * @author Created by Administrator on 2017/6/16.
 */
public interface IntEnum<E extends Enum<E>> {

    int getCode();

    int getIntValue();
}
