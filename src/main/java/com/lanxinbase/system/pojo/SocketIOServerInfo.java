package com.lanxinbase.system.pojo;

/**
 * Created by alan on 2018/6/2.
 */
public class SocketIOServerInfo {
    private String host;
    private Integer port;
    private Integer startTime;

    public SocketIOServerInfo(){

    }

    public SocketIOServerInfo(String h,int p){
        this.host = h;
        this.port = p;
        this.startTime = (int)(System.currentTimeMillis()/1000);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }
}
