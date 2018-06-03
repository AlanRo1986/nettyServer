package com.lanxinbase.socket.UDP;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by alan on 2018/6/3.
 */
public class UDPServer {
    private static final Logger logger = LoggerFactory.getLogger(UDPClient.class);
    private NioEventLoopGroup group;
    private Bootstrap bootstrap;
    private int port;

    public UDPServer(int port) {
        this.port = port;
    }

    public void run(){
        group = new NioEventLoopGroup(1);

        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST,true)
                .handler(new UDPServerChannelInitializer())
                .localAddress(new InetSocketAddress(port));

        try {
            logger.info("UDP Server [{}] is runing...",port);
            Channel channel = bootstrap.bind().syncUninterruptibly().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        group.shutdownGracefully().syncUninterruptibly();
    }

    public static void main(String[] args){
        int port = 9001;

        UDPServer udpServer = new UDPServer(port);
        udpServer.run();
    }

}
