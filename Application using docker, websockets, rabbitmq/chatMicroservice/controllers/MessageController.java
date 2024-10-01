package com.example.chatMicroservice.controllers;

import com.example.chatMicroservice.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(value = "/chat")
public class MessageController {
    @Autowired
    private MessageService messageService;


}
