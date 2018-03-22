package com.device.manage.web.service;

import com.device.api.entity.HouseTrend;
import com.device.api.entity.Page;
import com.device.api.entity.Result;
import com.device.api.uitls.CheckUtil;
import com.device.dao.HouseTrendMapper;
import com.device.manage.core.utils.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description: 房价趋势服务类
 * @Date: 2018-03-20 16:51
 */
@Service
public class HouseTrendService implements BaseService<HouseTrend> {

    @Resource
    private HouseTrendMapper houseTrendMapper;

    @Override
    public Page findPage(Page page) {
        long total = houseTrendMapper.selectCountByPage(page);
        List<HouseTrend> list = houseTrendMapper.selectByPage(page);
        page.setTotal(CheckUtil.isEmpty(total) ? 0 : total);
        page.setResult(list);
        return page;
    }

    @Transactional
    public Result insertBatch(List<HouseTrend> list) {
        try {
            Date date = list.get(0).getCreateTime();
            houseTrendMapper.delBatch(DateUtil.dateFormat(date));
            int i = houseTrendMapper.insertBatch(list);
            return i > 0 ? Result.success() : Result.failure();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<HouseTrend> selectAll() {
        return null;
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return 0;
    }

    @Override
    public int insert(HouseTrend obj) {
        return 0;
    }

    @Override
    public int insertSelective(HouseTrend obj) {
        return 0;
    }

    @Override
    public HouseTrend selectByPrimaryKey(Long id) {
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(HouseTrend obj) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(HouseTrend obj) {
        return 0;
    }
}
