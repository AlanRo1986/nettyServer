package com.lanxinbase.model;

/**
 * Created by alan on 2018/6/2.
 */
public class MessageIOModel extends Model {

    private String key;
    private String message;

    public MessageIOModel(){}

    public MessageIOModel(String k,String m){
        this.key = k;
        this.message = m;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
