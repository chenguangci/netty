package com.config;

/**
 * webSocket访问路径统一配置
 */
public class WebSocketURLConfig {

    //客户端接收服务端消息的地址的前缀信息
    public static final String RECEIVE_SERVER = "/topic";

    //客户端接收服务端消息的地址的前缀信息
    public static final String RECEIVE_SERVER_2 = "/user";

    //客户端给服务端发消息的地址的前缀
    public static final String SEND_TO_SERVER = "/client";

    //定义一对一推送的时候前缀
    public static final String ONE_TO_ONE = "/user";

    //配置endpoint
    public static final String END_POINT = "/webSocket";
}
