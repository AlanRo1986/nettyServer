package com.lanxinbase.socket;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;


public class SocketIOServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final Logger logger = Logger.getLogger(SocketIOServerChannelInitializer.class.getName());

    @Autowired
    private SocketIOServerHandler socketIOServerHandler;

    private boolean ok = false;

    public SocketIOServerChannelInitializer() {

    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
//        pipeline.addFirst("idleStateHandler", new IdleStateHandler(10, 0, 0));
//        pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));

//        pipeline.addLast("decoder",new StringDecoder(Charset.forName("utf-8")));
//        pipeline.addLast("encoder",new StringEncoder(Charset.forName("utf-8")));
        pipeline.addLast(socketIOServerHandler);

    }
}
