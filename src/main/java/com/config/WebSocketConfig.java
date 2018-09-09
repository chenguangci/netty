package com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint(WebSocketURLConfig.END_POINT).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //客户端接收服务端消息的地址的前缀信息
        registry.enableSimpleBroker(WebSocketURLConfig.RECEIVE_SERVER_2, WebSocketURLConfig.RECEIVE_SERVER);
        //客户端给服务端发消息的地址的前缀
        registry.setApplicationDestinationPrefixes(WebSocketURLConfig.SEND_TO_SERVER);
        //定义一对一推送的时候前缀
        registry.setUserDestinationPrefix(WebSocketURLConfig.ONE_TO_ONE);
    }

    /**
     * 配置客户端入站通道拦截器
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new UserInterceptor());
    }


}
