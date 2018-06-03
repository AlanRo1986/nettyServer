package com.lanxinbase.constant;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by alan on 2018/6/2.
 */
public class Constant {

    public static final String KAFKA_TOPIC_DEFAULT = "SOCKET_IO_TOPIC";//接受socket.id客户端消息
    public static final String KAFKA_TOPIC_SERVER = "SERVER_START_TOPIC";//socket.io服务器启动成功后发送一条消息到此频道
    public static final String KAFKA_TOPIC_SEND = "SEND_MSG_TOPIC";//发送socket.io消息

    public static final Long SESSION_TIME_OUT = 1800L;

    public class Redis {
        @Value("${spring.redis.host}")
        public static final String IP = "127.0.0.1";

        @Value("${spring.redis.port}")
        public static final int PORT = 6379;
    }
}
