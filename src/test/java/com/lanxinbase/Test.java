package com.lanxinbase;

import com.lanxinbase.system.pojo.ChannelMessage;

import java.util.concurrent.*;

/**
 * Created by alan on 2018/6/2.
 */
public class Test {
    public static void main(String[] args){

        Object message = new ChannelMessage("222");
        System.out.println((message instanceof String));
        System.out.println((message instanceof ChannelMessage));


    }
}
