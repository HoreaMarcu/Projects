package com.example.devicemanagement;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessagePublisher {

    @Autowired
    private RabbitTemplate template;

    @PostMapping("/sendData")
    public String publishMessage(@RequestBody MessageRabbitMQ message) {
        template.convertAndSend("rabbitExchange",
                "rabbitKey", message);

        return "Sent message with success";
    }
}