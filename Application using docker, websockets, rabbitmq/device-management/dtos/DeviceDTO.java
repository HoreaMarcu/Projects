package com.example.devicemanagement.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class DeviceDTO {

    private UUID id;
    private String description;
    private int maxHourlyEnergyConsumption;

    private PersonDTO personDTO;

}
