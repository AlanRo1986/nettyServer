package com.lanxinbase.system.pojo;

import com.lanxinbase.constant.Cmd;
import com.lanxinbase.model.Model;
import com.lanxinbase.system.utils.DateUtils;

/**
 * Created by alan on 2018/6/3.
 */
public class ChannelMessage extends Model {

    /**
     * view cmd constant.
     */
    private String cmd;

    /**
     * key of the socket client token.
     */
    private String key;

    /**
     * message send to kafka record time.
     */
    private Integer time;

    /**
     * ChannelRead ByteBuf to string.
     */
    private String message;

    public ChannelMessage(String key) {
        this(Cmd.CMD_HEARTBEAT,key);
    }

    public ChannelMessage(String cmd,String key) {
        this(cmd, key,null);
    }

    public ChannelMessage(String cmd,String key, String message) {
        this(cmd, key,DateUtils.getTimeInt(),message);
    }

    public ChannelMessage(String cmd, String key, Integer time, String message) {
        this.cmd = cmd;
        this.key = key;
        this.time = time;
        this.message = message;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
