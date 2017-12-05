package com.device.manage.web.service.log;


import com.device.api.entity.OperationLog;
import com.device.api.enums.OperLog;
import com.device.manage.web.exception.ServiceException;
import com.device.manage.web.service.BaseService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Created by Administrator on 2017/6/22.
 */
public interface OperationLogService extends BaseService<OperationLog> {

    /**
     * 添加常规操作日志
     *
     * @param request      获取Ip用
     * @param operTypeEnum 日志类型(枚举值)
     * @param content      日志内容
     */
    void addNormalOperLog(HttpServletRequest request, OperLog operTypeEnum, String content);

    void addNormalOperLog(HttpServletRequest request, Long uid, OperLog operTypeEnum, String content);

    OperationLog selectOperationLog(Long userId) throws ServiceException;
}
