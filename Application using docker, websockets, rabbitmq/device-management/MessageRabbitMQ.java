package com.example.devicemanagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageRabbitMQ {

    private String timestamp;
    private UUID device_id;
    private String measurement_value;
    private Integer maxHourlyEnergyConsumption;

}