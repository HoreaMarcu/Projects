package com.example.chatMicroservice;


import com.example.chatMicroservice.entities.Message;
import com.example.chatMicroservice.repositories.MessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HandlerWebSocket extends TextWebSocketHandler {

    private static final List<WebSocketSession> sessions = new ArrayList<>();
    @Autowired
    private MessageRepository messageRepository;
    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        sessions.add(session);
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Message> messages = (ArrayList<Message>) messageRepository.findAll();
        for(Message message : messages){
            try {
                String jsonMessage = objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(jsonMessage));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Session added");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        sessions.remove(session);
        System.out.println("Session removed");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        System.out.println("Message received: " + message.getPayload());
        ObjectMapper objectMapper = new ObjectMapper();
        Message message1 = objectMapper.readValue(message.getPayload(), Message.class);
        messageRepository.save(message1);
        System.out.println(message1.getMessage());
        sendMessageToAll(message.getPayload());
        session.sendMessage(new TextMessage("Server says: " + message.getPayload()));
    }

    public static void sendMessageToAll(String message) throws IOException {
        for (WebSocketSession session : sessions) {
            session.sendMessage(new TextMessage(message));
        }
    }

}
