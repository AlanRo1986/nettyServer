package com.lanxinbase.socket;

import com.lanxinbase.constant.Cmd;
import com.lanxinbase.constant.Constant;
import com.lanxinbase.services.resources.IKafkaService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.ByteProcessor;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Created by alan on 2018/6/2.
 */
@ChannelHandler.Sharable
public class SocketIOServerHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = Logger.getLogger(SocketIOServerHandler.class.getName());

    @Autowired
    private IKafkaService kafkaService;

    @Autowired
    private ChannelHandlerProvider handlerProvider;

    public SocketIOServerHandler() {

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        handlerProvider.add(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        if (this.check(ctx,buf)){
            String message = buf.toString(CharsetUtil.UTF_8);
            logger.info("socket io server received " + ctx.channel().remoteAddress() + ":" + message);

            /**
             * 组装成生产消息，发送到HTTP业务服务器处理业务。
             */
            handlerProvider.send2Kafka(Cmd.CMD_ROUTINE,handlerProvider.getContextKey(ctx),message);
        }
        ReferenceCountUtil.release(msg);
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
//                .addListener(ChannelFutureListener.CLOSE);
//        Channel channel = ctx.channel();
//        channel.write(Unpooled.copiedBuffer("ok".getBytes()));
//        channel.flush();
        handlerProvider.heartbeat(ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("ok".getBytes()));
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        handlerProvider.remove(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        ctx.disconnect();
    }

    private boolean check(ChannelHandlerContext ctx, ByteBuf buf) {
//        buf.forEachByte(new ByteProcessor() {
//            @Override
//            public boolean process(byte b) throws Exception {
//                return b=="b".getBytes()[0];
//            }
//        });
        return true;
    }

}
