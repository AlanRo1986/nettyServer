package com.lanxinbase.system.core;

import com.lanxinbase.constant.Cmd;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Set;

/**
 * Created by alan on 2018/6/2.
 */
public interface IChannelHandlerProvider {

    ChannelHandlerContext get(String key);

    int add(ChannelHandlerContext ctx);

    int remove(String key);

    int remove(ChannelHandlerContext ctx);

    void send(String key,String message);

    void send(String key,ByteBuf message);

    Set<String> getkeys();

    String getContextKey(ChannelHandlerContext ctx);

    void heartbeat(ChannelHandlerContext ctx);

    void send2Kafka(String cmd,String key,String message);
}
