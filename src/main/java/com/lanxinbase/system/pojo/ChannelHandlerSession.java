package com.lanxinbase.system.pojo;

import com.lanxinbase.constant.Constant;

import java.io.Serializable;

/**
 * Created by alan on 2018/4/1.
 */
public class ChannelHandlerSession implements Serializable {

    private static final long serialVersionUID = 2157415548415148L;

    private String key;

    private String imei;

    private Long startTime;

    private Long lastTime;

    private Long expiredTime;

    private String startTimeFormat;

    private String lastTimeFormat;

    private String expiredTimeFormat;

    public ChannelHandlerSession(){

    }

    /**
     * Socket io client session object.
     * @param key socket token.
     * @param imei 这个有点特殊，应该交给业务处理服务器进行赋值，业务服务器通过cmd指令‘routine’可以获得消息载体数据，
     *             其中包含imei序列号（与数据库对应），然后进行缓存修改。
     *             其中心跳&登陆&注销是不包含消息载体的，所以在收到cmd登陆指令后，应该先存储一份cache，其中缓存应该有一个
     *             字段：isCheckIn.当cmd指令‘routine’到达后，判断isCheckIn==true，if false。那么进行数据更新，
     *             比如：更新其在线状态。
     */
    public ChannelHandlerSession(String key, String imei){
        this.key = key;
        this.startTime = System.currentTimeMillis();
        this.lastTime = startTime;
        this.imei = imei;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getLastTime() {
        return lastTime;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
        this.expiredTime = this.lastTime + Constant.SESSION_TIME_OUT;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getStartTimeFormat() {
        return startTimeFormat;
    }

    public void setStartTimeFormat(String startTimeFormat) {
        this.startTimeFormat = startTimeFormat;
    }

    public String getLastTimeFormat() {
        return lastTimeFormat;
    }

    public void setLastTimeFormat(String lastTimeFormat) {
        this.lastTimeFormat = lastTimeFormat;
    }

    public Long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getExpiredTimeFormat() {
        return expiredTimeFormat;
    }

    public void setExpiredTimeFormat(String expiredTimeFormat) {
        this.expiredTimeFormat = expiredTimeFormat;
    }
}
