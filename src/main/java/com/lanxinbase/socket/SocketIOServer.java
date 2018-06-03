package com.lanxinbase.socket;

import com.lanxinbase.constant.Constant;
import com.lanxinbase.services.resources.IKafkaService;
import com.lanxinbase.socket.web.WebSocketServerChannelInitializer;
import com.lanxinbase.system.pojo.SocketIOServerInfo;
import com.lanxinbase.system.provider.CacheProvider;
import com.lanxinbase.system.utils.JsonUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

/**
 * Created by alan on 2018/6/2.
 */
public class SocketIOServer implements InitializingBean,DisposableBean,Runnable {

    private Logger logger = Logger.getLogger(SocketIOServer.class.getName());
    private int port = -1;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private Thread thread;

    @Autowired
    private SocketIOServerChannelInitializer socketIOServerChannelInitializer;

    @Autowired
    private CacheProvider cacheProvider;

    @Autowired
    private IKafkaService kafkaService;

    public SocketIOServer(){

    }

    public SocketIOServer(int port){
        this.port = port;
    }

    public void start(){
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(32);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .localAddress(new InetSocketAddress(this.port))
                    .childHandler(this.socketIOServerChannelInitializer);
//                    .childHandler(new WebSocketServerChannelInitializer());WebSocket support.

            logger.info("SocketIOServer["+this.port+"] start....." );

            ChannelFuture f = bootstrap.bind().sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        thread = new Thread(this);
        thread.start();

        /**
         * 发送一条消息到kafka中，让节点服务器缓存此服务器信息
         */
        String host = InetAddress.getLocalHost().getHostAddress();
        SocketIOServerInfo info = new SocketIOServerInfo(host,this.port);
        kafkaService.send(JsonUtils.ObjectToJson(info,true), Constant.KAFKA_TOPIC_SERVER);

        /**
         * clear cache.
         */
        cacheProvider.clear();
    }

    @Override
    public void destroy() throws Exception {
        bossGroup.shutdownGracefully().syncUninterruptibly();
        workerGroup.shutdownGracefully().syncUninterruptibly();
        thread.interrupt();
    }

    @Override
    public void run() {
        this.start();
    }
}
