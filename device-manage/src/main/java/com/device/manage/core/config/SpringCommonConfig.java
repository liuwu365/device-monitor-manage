package com.device.manage.core.config;

import com.device.manage.core.utils.LogWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rocking on 2016/9/12.
 */
@ImportResource({
        "classpath:application-dubbo-client.xml",
        "classpath:application-context.xml",
        "classpath:spring-shiro.xml"
})
@Import({ServerPropertiesAutoConfiguration.class,
        DispatcherServletAutoConfiguration.class,
        EmbeddedServletContainerAutoConfiguration.class})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, MultipartAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
public class SpringCommonConfig {
    private static final Logger logger = LoggerFactory.getLogger(SpringCommonConfig.class);

    @Bean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
    @ConditionalOnMissingBean(MultipartResolver.class)
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver() {
            public boolean isMultipart(HttpServletRequest request) {
                String uri = request.getRequestURI();
                if (uri.indexOf("ueditor/jsp") > 0 && "post".equalsIgnoreCase(request.getMethod())) {
                    logger.info(uri);
                    return false;
                } else {
                    return super.isMultipart(request);
                }
            }
        };
    }

    @Bean
    public LogWriter logWriter(
            @Value("${log.write_to_file}") boolean writeToFile,
            @Value("${log.path}") String logPath,
            @Value("${log.number}") int logNumber
    ) {
        return new LogWriter(writeToFile, logPath, logNumber);
    }


    @Bean
    private MessageSource messageSource() {
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        reloadableResourceBundleMessageSource.setBasenames("classpath:message");
        reloadableResourceBundleMessageSource.setDefaultEncoding("UTF-8");
        reloadableResourceBundleMessageSource.setUseCodeAsDefaultMessage(false);
        reloadableResourceBundleMessageSource.setCacheSeconds(60);
        return reloadableResourceBundleMessageSource;
    }

}
