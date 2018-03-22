package com.device.dao;

import com.device.api.entity.HouseTrend;
import com.device.api.entity.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HouseTrendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(HouseTrend record);

    int insertSelective(HouseTrend record);

    HouseTrend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HouseTrend record);

    int updateByPrimaryKey(HouseTrend record);

    /**
     * 分页查询信息
     *
     * @param page
     * @return
     */
    List<HouseTrend> selectByPage(Page<HouseTrend> page);

    long selectCountByPage(Page<HouseTrend> page);

    int insertBatch(List<HouseTrend> list);

    int delBatch(@Param("date")String date);

}