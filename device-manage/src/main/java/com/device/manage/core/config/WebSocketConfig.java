package com.device.manage.core.config;

import com.device.manage.core.handler.SpringWebSocketHandler;
import com.device.manage.core.interceptor.webSocketInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Component
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
      
    @Override  
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/webSocket/socketServer.json").addInterceptors(new webSocketInterceptor());
        registry.addHandler(webSocketHandler(), "/webSocket/socketServer/sockJs.json").addInterceptors(new webSocketInterceptor()).withSockJS();
    }

    @Bean
    public SpringWebSocketHandler webSocketHandler () {
        return new SpringWebSocketHandler();
    }
  
}  