package com.lanxinbase.socket.web;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;


/**
 * Created by alan on 2018/6/3.
 */
@ChannelHandler.Sharable
public class WebSocketServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final String WEBSOCKET_PATH = "/websocket";

    /**
     * you can open the web url(http://127.0.0.1:8888/) try.
     */
    public WebSocketServerChannelInitializer() {

    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline cp = ch.pipeline();
        cp.addFirst(new HttpServerCodec());
        cp.addLast(new HttpObjectAggregator(65536));
        cp.addLast(new ChunkedWriteHandler());
        cp.addLast(new WebSocketServerCompressionHandler());
        cp.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
        cp.addLast(new WebSocketIndexPageHandler(WEBSOCKET_PATH));
        cp.addLast(new WebSocketServerHandler());
    }
}
