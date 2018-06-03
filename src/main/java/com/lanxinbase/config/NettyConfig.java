package com.lanxinbase.config;

import com.lanxinbase.NettyApplication;
import com.lanxinbase.socket.SocketIOServerChannelInitializer;
import com.lanxinbase.socket.SocketIOServer;
import com.lanxinbase.socket.SocketIOServerHandler;
import io.netty.channel.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Created by alan on 2018/6/2.
 */

@ChannelHandler.Sharable
@Configuration
public class NettyConfig {

    private int port;

    public NettyConfig(){
        this.port = NettyApplication.port;
    }

    @Bean
    public SocketIOServer socketIOServer(){
        SocketIOServer socketIOServer = new SocketIOServer(this.port);
        return socketIOServer;
    }

    @Bean
    public SocketIOServerHandler socketIOServerHandler(){
        return new SocketIOServerHandler();
    }

    @Bean
    public SocketIOServerChannelInitializer socketIOChannelInitializer(){
        return new SocketIOServerChannelInitializer();
    }
}
