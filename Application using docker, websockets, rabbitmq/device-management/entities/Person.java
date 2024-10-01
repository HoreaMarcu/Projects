package com.example.devicemanagement.entities;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class Person {

    @Id
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "address")
    private String address;

    @Column(name = "age", nullable = false)
    private int age;
    @Column(name = "admin", nullable = false)
    private boolean adminRole;

    public Person(UUID id, String name, String password, int age, boolean adminRole) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.age = age;
        this.adminRole = adminRole;
    }

    public Person() {
    }
}
