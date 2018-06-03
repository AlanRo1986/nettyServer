package com.lanxinbase.services.impl;

import com.lanxinbase.constant.Cmd;
import com.lanxinbase.constant.Constant;
import com.lanxinbase.model.MessageIOModel;
import com.lanxinbase.services.resources.IKafkaService;
import com.lanxinbase.socket.ChannelHandlerProvider;
import com.lanxinbase.system.basic.CompactService;
import com.lanxinbase.system.exception.IllegalServiceException;
import com.lanxinbase.system.pojo.ChannelMessage;
import com.lanxinbase.system.utils.JsonUtils;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Created by alan on 2018/6/2.
 */

@Service
public class KafkaServiceImpl extends CompactService implements IKafkaService {

    private static final Logger logger = Logger.getLogger(KafkaServiceImpl.class.getName());

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private ChannelHandlerProvider channelHandlerProvider;

    public KafkaServiceImpl() {
        super(KafkaServiceImpl.class);
    }

    @Override
    public void send(Object message, String topic) throws IllegalServiceException {

        String key = topic + "_" + message.hashCode();
        String valueString = message.toString();

        if (!(message instanceof String)){
            valueString = JsonUtils.ObjectToJson(message,true);
        }
        if (topic == null){
            topic = Constant.KAFKA_TOPIC_DEFAULT;
        }

        kafkaTemplate.send(topic,key,valueString);
//        kafkaTemplate.execute(new KafkaOperations.ProducerCallback() {
//            @Override
//            public Object doInKafka(Producer producer) {
//                logger.info("doInKafka");
//                return null;
//            }
//        });

        /**
         * 生产监听
         */
        kafkaTemplate.setProducerListener(new ProducerListener() {
            @Override
            public void onSuccess(String topic, Integer partition, Object key, Object value, RecordMetadata recordMetadata) {
//                logger.info(String.format("onSuccess{topic:%s,partition:%s,%s=%s}",topic,partition,key.toString(),value));
            }

            @Override
            public void onError(String topic, Integer partition, Object key, Object value, Exception exception) {
                logger.info(String.format("onError{topic:%s,partition:%s,%s=%s}",topic,partition,key.toString(),value));
            }
        });
    }

    /**
     * receiver default.
     * @param message
     * @throws IllegalServiceException
     */
    @KafkaListener(topics = Constant.KAFKA_TOPIC_DEFAULT)
    @Override
    public void receiver(String message) throws IllegalServiceException {
        logger.info("receiver:"+message);
        if (JsonUtils.isJson(message)){
            ChannelMessage m = JsonUtils.JsonToObject(message,ChannelMessage.class);
            if (m != null){
                switch (m.getCmd()){
                    case Cmd.CMD_LOGIN:
                        /**
                         * 登陆指令，肯定要对其进行登陆数据处理，但是由于登陆指令并不包含对数据库对应的IMEI号，所以此时
                         * 应该实例化一个模型类，并设置isCheckIn为false，然后记录到缓存中。
                         */
                        logger.info(String.format("cmd:(%s) added to cache.",m.getKey()));
                        break;
                    case Cmd.CMD_LOGOUT:
                        /**
                         * 注销指令，当然是从缓存中删除数据，并且要更改数据库的在线状态为离线。
                         * getCache
                         * select by imei
                         * update isOnline = false by imei
                         */
                        logger.info(String.format("cmd:(%s) removed for cache.",m.getKey()));
                        break;
                    case Cmd.CMD_HEARTBEAT:
                        /**
                         * 心跳指令，更新缓存中的心跳时间，其实硬件服务器中的redis缓存已经更新过了，可以考虑不处理此消息。
                         */
                        logger.info(String.format("cmd:(%s) updated last time for cache.",m.getKey()));
                        break;

                    case Cmd.CMD_ROUTINE:
                        /**
                         * 常规消息指令，这里需要处理的主要是消息业务。
                         * 其次，应该优先判断缓存中的isCheckIn,如果等于false，那么则更新数据库，并更新缓存中的IMEI号。
                         * 以为常规消息指令包含了消息载体，消息载体包含了key&imei。
                         */
                        logger.info(String.format("cmd:(%s) updated for cache & mysql.",m.getKey()));
                        break;

                    default:
                        break;
                }
            }
        }

    }

    /**
     * send socket client message.
     * @param message json message.
     * @throws IllegalServiceException
     */
    @KafkaListener(topics = Constant.KAFKA_TOPIC_SEND)
    @Override
    public void receiver2(String message) throws IllegalServiceException {
        if (!JsonUtils.isJson(message)){
            return;
        }
        logger.info("send to socket:"+message);
        MessageIOModel msg = JsonUtils.JsonToObject(message,MessageIOModel.class);
        if (msg != null){
            channelHandlerProvider.send(msg.getKey(),msg.getMessage());
        }
    }
}
