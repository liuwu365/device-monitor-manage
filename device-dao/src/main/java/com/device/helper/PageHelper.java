package com.device.helper;



import com.device.api.entity.Page;
import com.device.base.BaseMapper;

/**
 * @author Created by Administrator on 2017/6/20.
 */
public class PageHelper<T>  {

    private BaseMapper<T> baseMapper;


    public PageHelper() {

    }

    public PageHelper(BaseMapper<T> baseMapper) {
        if (baseMapper == null) {
            throw new NullPointerException("baseMapper can not be null or empty");
        }
        this.baseMapper = baseMapper;
    }

    public Page<T> invoke(Page<T> page) {
        long count = baseMapper.selectCount(page.getFilter());
        page.setTotal(count);
        page.setResult(baseMapper.selectPage(page));
        return page;
    }
}
