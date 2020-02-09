/*
package com.zero.service;

import com.zero.dto.ChattingMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageProducerService {

    private static final String TOPIC_NAME = "ChattingMessage";

    private final KafkaTemplate<String, ChattingMessage> kafkaTemplate;

    @Autowired
    public KafkaMessageProducerService(KafkaTemplate<String, ChattingMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(ChattingMessage message) {
        kafkaTemplate.send(TOPIC_NAME, message);
    }
}
*/