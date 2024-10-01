package com.example.chatMicroservice.entities;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Message {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(name = "sender", nullable = false)
    private UUID senderId;

    @Column(name = "receiver", nullable = false)
    private UUID receiverId;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "seen", nullable = false)
    private boolean seen;
}