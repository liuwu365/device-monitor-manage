package com.device.base;




import com.device.api.entity.Page;

import java.util.List;
import java.util.Map;

/**
 * @author Created by Administrator on 2017/6/20.
 */
public interface BaseMapper<T> {
    //分页查询
    long selectCount(Map<String, Object> filter);
    List<T> selectPage(Page<T> page);
}
