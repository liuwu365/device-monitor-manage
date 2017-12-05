package com.device.dao;

import com.device.api.entity.OperationLog;
import com.device.api.entity.Page;
import com.device.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OperationLogMapper extends BaseMapper<OperationLog> {
    int deleteByPrimaryKey(Long id);

    int insert(OperationLog record);

    int insertSelective(OperationLog record);

    OperationLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OperationLog record);

    int updateByPrimaryKey(OperationLog record);

    OperationLog selectOperationLog(@Param("userId") Long userId);

    OperationLog selectOperationLog(@Param("userId") Long userId, @Param("operType") Integer operType);

    /**
     * 用户操作记录查询
     * @param page
     * @return
     */
    List<OperationLog> selectByOperationLogInfo(Page page);

    long selectOperationLogCount(Page page);
}