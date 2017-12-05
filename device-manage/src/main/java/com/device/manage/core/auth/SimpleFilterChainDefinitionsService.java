package com.device.manage.core.auth;

import org.apache.shiro.config.Ini;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Created by Administrator on 2017/6/27.
 */
@Component
public class SimpleFilterChainDefinitionsService extends AbstractFilterChainDefinitionsService {

	@Override
	public Map<String, String> initOtherPermission() {


		try {

			return (Ini.Section) beanFactory.getBean("chainDefinitionSectionMetaSource");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
