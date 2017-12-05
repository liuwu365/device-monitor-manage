package com.device.manage.web.service.permission;

import com.device.api.entity.Page;
import com.device.api.entity.backrole.BackIpLimit;
import com.device.manage.web.service.BaseService;

public interface BackIpLimitService extends BaseService<BackIpLimit> {

    /**
     * 后台ip限制列表
     *
     * @param page
     * @return
     */
    Page<BackIpLimit> getPage(Page page);

    /**
     * ipAddress是否存在
     *
     * @param ipAddress
     * @return 存在：true
     */
    boolean isExisit(String ipAddress);


}
