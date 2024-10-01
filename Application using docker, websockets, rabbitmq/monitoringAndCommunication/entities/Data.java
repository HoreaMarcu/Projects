package com.example.monitoringAndCommunication.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Data {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(name = "timestamp", nullable = false)
    private String timestamp;

    @Column(name = "device_id", nullable = false)
    private UUID device_id;

    @Column(name = "measurement_value", nullable = false)
    private String measurement_value;

    public Data(String timestamp, UUID device_id, String measurement_value) {
        this.timestamp = timestamp;
        this.device_id = device_id;
        this.measurement_value = measurement_value;
    }

    public Data() {
    }
}
