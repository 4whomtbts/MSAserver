package com.zero.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class KafkaWebController {

    @Autowired
    KafkaSender kafkaSender;

//
    //@RequestMapping(method = RequestMethod.GET, path = "/kafka")
@PostMapping("/kafka/{topicName}")
    public String sendToTopic(@PathVariable String topicName, @RequestBody String message) {
        kafkaSender.send(topicName, message);
        return "Message sent";
    }
}
