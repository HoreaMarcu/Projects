package com.example.devicemanagement.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class PersonDTO {
    private UUID id;
    private String name;
    private String password;
    private int age;

    private boolean adminRole;
}
