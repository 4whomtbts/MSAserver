package com.zero.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaReceiver {

    @KafkaListener(topics = "topic1")
    public void receivedTopic(ConsumerRecord<?, ?> consumerRecord) {
        log.info("GET TOPIC1 = " + consumerRecord.toString());
    }

    @KafkaListener(topics = "test")
    public void receiveTopicTest(ConsumerRecord<?, ?> consumerRecord) {
        log.info("GET TEST = " + consumerRecord.toString());
    }

    /*
    @KafkaListener(topics = "topic2")
    public void receiveTopic2(ConsumerRecord<?, ?> consumerRecord) {
        log.info("GET TOPIC2 = " + consumerRecord.toString());
    }

     */
}