package com.lanxinbase.socket.UDP;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by alan on 2018/6/3.
 */
public class UDPServerHandler extends SimpleChannelInboundHandler<LogEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UDPServerHandler.class);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.info("added a upd client {}",ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LogEvent logEvent) throws Exception {
        logger.info("received[{}]:{}->{}",logEvent.getAddress(),logEvent.getLogFile(),logEvent.getMsg());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.info("removed a udp client {}",ctx.channel().remoteAddress());
    }
}
