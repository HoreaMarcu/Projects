package com.example.chatMicroservice.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class MessageDto {
    private UUID senderId;
    private UUID receiverId;
    private String message;
    private boolean seen;
}
