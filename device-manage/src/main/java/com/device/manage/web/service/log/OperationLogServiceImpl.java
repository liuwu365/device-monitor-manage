package com.device.manage.web.service.log;

import com.device.api.entity.OperationLog;
import com.device.api.entity.Page;
import com.device.api.enums.OperLog;
import com.device.api.uitls.CheckUtil;
import com.device.api.uitls.ErrorWriterUtil;
import com.device.dao.OperationLogMapper;
import com.device.manage.core.utils.IpUtil;
import com.device.manage.core.utils.SessionUtil;
import com.device.manage.web.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author Created by Administrator on 2017/6/22.
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {
    Logger logger = LoggerFactory.getLogger(OperationLogServiceImpl.class);

    @Resource
    private OperationLogMapper operationLogMapper;

    @Resource
    private HttpServletRequest request;

    @Override
    public void addNormalOperLog(HttpServletRequest request, OperLog operTypeEnum, String content) {
        addNormalOperLog(request, null, operTypeEnum, content);
    }

    @Override
    public void addNormalOperLog(HttpServletRequest request, Long uid, OperLog operTypeEnum,String content) {
        try {
            if (!CheckUtil.isEmpty(SessionUtil.getCurrentUser())) {
                OperationLog operationLog = new OperationLog();
                operationLog.setOperUserId(SessionUtil.getCurrentUser().getId());
                operationLog.setOperUserName(SessionUtil.getCurrentUser().getUserName());
                operationLog.setTargetUid(uid);
                operationLog.setIpAddress(IpUtil.getIpAddress(request));
                operationLog.setOperType(operTypeEnum.getCode());
                operationLog.setCreateTime(new Date());
                operationLog.setContent(content);
                operationLogMapper.insertSelective(operationLog);
            }
        } catch (Exception e) {
            logger.error("插入常规操作日志时异常:{}", ErrorWriterUtil.writeError(e));
        }
    }

    @Override
    public OperationLog selectOperationLog(Long userId) throws ServiceException {
        try {
            return operationLogMapper.selectOperationLog(userId);
        } catch (Exception e) {
            logger.error("selectOperationLog error|userId|{}|error|{}", userId, ErrorWriterUtil.writeError(e));
            throw new ServiceException("数据查询错误,请稍后再试！");
        }
    }

    @Override
    public Page<OperationLog> findPage(Page<OperationLog> page) {
        return null;
    }

    @Override
    public List<OperationLog> selectAll() {
        return null;
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return operationLogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(OperationLog obj) {
        return operationLogMapper.insert(obj);
    }

    @Override
    public int insertSelective(OperationLog obj) {
        return operationLogMapper.insertSelective(obj);
    }

    @Override
    public OperationLog selectByPrimaryKey(Long id) {
        return operationLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OperationLog obj) {
        return operationLogMapper.updateByPrimaryKeySelective(obj);
    }

    @Override
    public int updateByPrimaryKey(OperationLog obj) {
        return operationLogMapper.updateByPrimaryKey(obj);
    }

}
