package com.lanxinbase.socket.UDP;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

/**
 * Created by alan on 2018/6/3.
 */
public class UDPClient {

    private static final Logger logger = LoggerFactory.getLogger(UDPClient.class);

    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final File file;

    public UDPClient(InetSocketAddress address, File file) {
        this.group = new NioEventLoopGroup(1);
        this.file = file;

        this.bootstrap = new Bootstrap();
        this.bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new LogEventEncoder(address));
    }


    public void run() throws Exception {

        Channel ch = bootstrap.bind(0).sync().channel();
        long pos = 0;
        for (; ; ) {
            long len = file.length();
            if (len < pos) {
                pos = len;
            } else if (len > pos) {
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                raf.seek(pos);

                String line;
                while ((line = raf.readLine()) != null) {
                    ch.writeAndFlush(new LogEvent(null, file.getAbsolutePath(), line, -1));
                    logger.info("send upd data{}.",pos);

                }
                pos = raf.getFilePointer();
                raf.close();
            }
        }

    }

    public void destroy() {
        group.shutdownGracefully().syncUninterruptibly();
    }

    public static void main(String[] args) {
        int port = 9000;
        String path = "d:\\1.log";
        UDPClient udp = new UDPClient(new InetSocketAddress("255.255.255.255", port), new File(path));

        try {
            udp.run();
        } catch (Exception e) {
            e.printStackTrace();
            udp.destroy();
        }
    }
}
