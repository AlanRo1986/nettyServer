package com.lanxinbase.app;

import com.lanxinbase.constant.Constant;
import com.lanxinbase.model.MessageIOModel;
import com.lanxinbase.services.resources.IKafkaService;
import com.lanxinbase.socket.ChannelHandlerProvider;
import com.lanxinbase.system.core.ResultResp;
import com.lanxinbase.system.exception.IllegalServiceException;
import com.lanxinbase.system.provider.CacheProvider;
import com.lanxinbase.system.utils.DateUtils;
import com.lanxinbase.system.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by alan on 2018/6/2.
 */

@RestController
public class TestController {

    @Autowired
    private IKafkaService kafkaService;

    @Autowired
    private CacheProvider cacheProvider;

    /**
     * test kafka send message.
     * @param request
     * @return
     */
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public ResultResp<Void> test(HttpServletRequest request){
        try {
            kafkaService.send("test message time:"+ DateUtils.getTime(), Constant.KAFKA_TOPIC_DEFAULT);
        } catch (IllegalServiceException e) {
            e.printStackTrace();
        }
        return new ResultResp<>();
    }

    /**
     * 获取所有的Socket Handler context.
     * @param request
     * @return
     */
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public ResultResp<Map<String,Object>> all(HttpServletRequest request){
        ResultResp<Map<String,Object>> resp = new ResultResp<>();
        resp.setData((Map<String, Object>) cacheProvider.getAll());
        return resp;
    }

    /**
     * 发送消息到socket client
     * @param request
     * @return
     */
    @RequestMapping(value = "/send",method = RequestMethod.GET)
    public ResultResp<Void> send(HttpServletRequest request){
        ResultResp<Void> resp = new ResultResp<>();

        String key = request.getParameter("key");
        String msg = request.getParameter("msg");
        MessageIOModel m = new MessageIOModel(key,msg);
        try {
            kafkaService.send(JsonUtils.ObjectToJson(m,true),Constant.KAFKA_TOPIC_SEND);
        } catch (IllegalServiceException e) {
            e.printStackTrace();
        }
        return resp;
    }

}
