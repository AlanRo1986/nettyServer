package com.lanxinbase.socket;

import com.lanxinbase.services.resources.IKafkaService;
import com.lanxinbase.system.core.IChannelHandlerProvider;
import com.lanxinbase.system.exception.IllegalServiceException;
import com.lanxinbase.system.pojo.ChannelMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by alan on 2018/6/2.
 */
@Component
public class ChannelHandlerProvider implements IChannelHandlerProvider {

    private Logger logger = Logger.getLogger(SocketIOServerHandler.class.getName());

    @Autowired
    private IKafkaService kafkaService;

    private Map<String, ChannelHandlerContext> contexts;

    public ChannelHandlerProvider() {
        this.contexts = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public ChannelHandlerContext get(String key) {
        if (contexts.containsKey(key)) {
            return contexts.get(key);
        }
        return null;
    }

    /**
     * add a socket client.
     * @param ctx socket client context.
     * @return int
     */
    @Override
    public int add(ChannelHandlerContext ctx) {
        logger.info("add a context " + this.getContextKey(ctx));
        contexts.put(this.getContextKey(ctx), ctx);
        return this.getkeys().size();
    }

    /**
     * remove a socket client.
     * @param key socket client context by key.
     * @return int
     */
    @Override
    public int remove(String key) {
        if (contexts.containsKey(key)) {
            logger.info("remove a context " + key);
            this.get(key).close();
            this.get(key).disconnect();
            contexts.remove(key);
        }
        return this.getkeys().size();
    }

    /**
     * remove a socket client.
     * @param ctx socket client context by key.
     * @return int
     */
    @Override
    public int remove(ChannelHandlerContext ctx) {
        return this.remove(this.getContextKey(ctx));
    }

    /**
     * Send a message to socket by key.
     * @param key the socket client string key.
     * @param message Message String type.
     */
    @Override
    public void send(String key, String message) {
        this.send(key, Unpooled.copiedBuffer(message.getBytes()));
    }

    /**
     * Send a message to socket by key.
     * @param key the socket client string key.
     * @param buf Message ByteBuf type.
     */
    @Override
    public void send(String key, ByteBuf buf) {
        if (contexts.containsKey(key)){
            this.get(key).writeAndFlush(buf);
        }
    }

    /**
     * returen all keys of socket context.
     * @return Set<String>
     */
    @Override
    public Set<String> getkeys() {
        return contexts.keySet();
    }

    /**
     * return a string key of socket client context.
     * @param ctx socket client context.
     * @return string
     */
    @Override
    public String getContextKey(ChannelHandlerContext ctx) {
        if (ctx == null){
            return null;
        }
        return ctx.channel().remoteAddress().toString();
    }

    /**
     * 心跳更新
     * 这里直接用AOP处理，所以代码是空的
     * @param ctx socket client context
     */
    @Override
    public void heartbeat(ChannelHandlerContext ctx) {

    }

    /**
     * send message to kafka
     * @param cmd message cmd code.
     * @param key the socket client of the key.
     * @param message the message body,you can set null.
     */
    @Override
    public void send2Kafka(String cmd, String key, String message) {
        try {
            kafkaService.send(new ChannelMessage(cmd,key,message),null);
        } catch (IllegalServiceException e) {
            e.printStackTrace();
        }
    }
}
