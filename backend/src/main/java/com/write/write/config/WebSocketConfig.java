package com.write.write.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket配置
 * 启用STOMP协议支持实时聊天
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 启用简单消息代理，配置前缀
        config.enableSimpleBroker("/topic", "/queue");
        // 设置应用目标前缀
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册原生 WebSocket 端点（浏览器原生 WebSocket 直连）
        registry.addEndpoint("/ws/chat")
            .setAllowedOrigins("*");

        // 同时保留 SockJS 端点（兼容旧客户端）
        registry.addEndpoint("/ws/chat")
            .setAllowedOrigins("*")
            .withSockJS();
    }
}
