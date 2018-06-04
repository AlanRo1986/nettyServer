package com.lanxinbase.system.aspects;

import com.lanxinbase.constant.Cmd;
import com.lanxinbase.services.resources.IKafkaService;
import com.lanxinbase.socket.ChannelHandlerProvider;
import com.lanxinbase.system.exception.IllegalServiceException;
import com.lanxinbase.system.pojo.ChannelHandlerSession;
import com.lanxinbase.system.pojo.ChannelMessage;
import com.lanxinbase.system.provider.CacheProvider;
import io.netty.channel.ChannelHandlerContext;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Created by alan on 2018/6/2.
 */
@Component
@Aspect
public class ChannelHandlerAspect {

    private static final Logger logger = Logger.getLogger(ChannelHandlerAspect.class.getName());

    @Autowired
    private ChannelHandlerProvider handlerProvider;

    @Autowired
    private CacheProvider cacheProvider;

    public ChannelHandlerAspect() {

    }

    @Pointcut("execution(public * com.lanxinbase.socket.ChannelHandlerProvider.add(..))&&args(ctx)")
    private void addHandler(ChannelHandlerContext ctx){

    }

    @Pointcut("execution(public * com.lanxinbase.socket.ChannelHandlerProvider.remove(..))&&args(ctx)")
    private void removeHandler(ChannelHandlerContext ctx){

    }

    @Pointcut("execution(public * com.lanxinbase.socket.ChannelHandlerProvider.heartbeat(..))&&args(ctx)")
    private void heartbeatHandler(ChannelHandlerContext ctx){

    }

    @Before("addHandler(ctx)")
    public void addBefore(ChannelHandlerContext ctx){
        logger.info("before add channel handler context.");
    }

    @After("addHandler(ctx)")
    public void addAfter(ChannelHandlerContext ctx){
        logger.info("after add channel handler context.");

        /**
         * add cache.
         */
        String key = handlerProvider.getContextKey(ctx);
        ChannelHandlerSession session = new ChannelHandlerSession(key,key);
        cacheProvider.put(session.getKey(),session,cacheProvider.EXPIRED_TIME);

        this.send2Kafka(Cmd.CMD_LOGIN,key);
    }

    @Before("removeHandler(ctx)")
    public void removeBefore(ChannelHandlerContext ctx){
        logger.info("before remove channel handler context.");
    }

    @After("removeHandler(ctx)")
    public void removeAfter(ChannelHandlerContext ctx){
        logger.info("after remove channel handler context.");

        /**
         * remove cache.
         */
        String key = handlerProvider.getContextKey(ctx);
        cacheProvider.remove(key);
        this.send2Kafka(Cmd.CMD_LOGOUT,key);
    }

    @Before("heartbeatHandler(ctx)")
    public void heartbeatBefore(ChannelHandlerContext ctx){
        logger.info("before heartbeat channel handler context.");
    }

    @After("heartbeatHandler(ctx)")
    public void heartbeatAfter(ChannelHandlerContext ctx){
        logger.info("after heartbeat channel handler context.");

        /**
         * update cache.
         */
        String key = handlerProvider.getContextKey(ctx);
        ChannelHandlerSession session = (ChannelHandlerSession) cacheProvider.get(key);
        if (session != null){
            session.setLastTime(System.currentTimeMillis());
            cacheProvider.put(session.getKey(),session,cacheProvider.EXPIRED_TIME);

            this.send2Kafka(Cmd.CMD_HEARTBEAT,key);
        }else {
            handlerProvider.remove(ctx);
        }
    }

    private void send2Kafka(String cmd,String key){
        handlerProvider.send2Kafka(cmd,key,null);
    }


}
