package com.device.manage.web.service.permission;

import com.device.api.entity.Result;
import com.device.api.entity.backrole.BackUser;

public interface BackInfoService {

	Result saveUserInfo(BackUser backUser, String roles);


}
