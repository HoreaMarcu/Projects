package com.example.monitoringAndCommunication;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HandlerWebSocket extends TextWebSocketHandler {

    private static final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        sessions.add(session);
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
        session.sendMessage(new TextMessage("Server says: " + message.getPayload()));
    }

    public static void sendMessageToAll(String message) throws IOException {
        for (WebSocketSession session : sessions) {
            session.sendMessage(new TextMessage(message));
        }
    }

}