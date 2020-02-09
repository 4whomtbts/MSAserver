package com.zero.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
public class KafkaSender {


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String payload) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, payload);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("Kafka Failure = " + ex);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Kafka Response " + result.getProducerRecord().value());
            }
        });
        //kafkaTemplate.send(topic, payload);
    }
}
