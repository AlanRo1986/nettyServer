package com.lanxinbase.services.resources;

import com.lanxinbase.system.exception.IllegalServiceException;

/**
 * Created by alan on 2018/6/2.
 */
public interface IKafkaService {

    void send(Object message,String topic) throws IllegalServiceException;

    void receiver(String message) throws IllegalServiceException;

    void receiver2(String message) throws IllegalServiceException;
}
