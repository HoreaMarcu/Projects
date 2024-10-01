package com.example.devicemanagement.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;
@Getter
@Setter
@Entity
public class Device {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "address")
    private String address;

    @Column(name = "maxHourlyEnergyConsumption", nullable = false)
    private Integer maxHourlyEnergyConsumption;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private Person user;



    public Device(String description, String address, Integer maxHourlyEnergyConsumption) {
        this.description = description;
        this.address = address;
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
    }

    public Device(String description, Integer maxHourlyEnergyConsumption, Person person) {
        this.description = description;
        this.maxHourlyEnergyConsumption = maxHourlyEnergyConsumption;
        this.user = person;
    }

    public Device() {

    }
}
