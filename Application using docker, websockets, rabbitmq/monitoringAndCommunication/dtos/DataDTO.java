package com.example.monitoringAndCommunication.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class DataDTO {
    private String timestamp;
    private UUID device_id;
    private String measurement_value;

}
