package com.lanxinbase.socket.UDP;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by alan on 2018/6/3.
 */
public class UDPServerChannelInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline channelPipeline = channel.pipeline();
        channelPipeline.addLast(new LogEventDecoder());
        channelPipeline.addLast(new UDPServerHandler());
    }
}
