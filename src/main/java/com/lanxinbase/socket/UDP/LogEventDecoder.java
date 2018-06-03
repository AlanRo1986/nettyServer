package com.lanxinbase.socket.UDP;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by alan on 2018/6/3.
 */
public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {

    private static final Logger logger = LoggerFactory.getLogger(UDPServerHandler.class);


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> list) throws Exception {
        ByteBuf buf = datagramPacket.content();
        logger.info(">>>>>>>"+buf.toString(CharsetUtil.UTF_8));
        int idx = buf.indexOf(0,buf.readableBytes(),LogEvent.SEPARATOR);

        String filename = buf.slice(0,idx).toString(CharsetUtil.UTF_8);
        String logMsg = buf.slice(idx+1,buf.readableBytes()).toString(CharsetUtil.UTF_8);

        LogEvent event = new LogEvent(datagramPacket.sender(),filename,logMsg,System.currentTimeMillis());
        list.add(event);
    }
}
