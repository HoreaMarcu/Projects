package com.example.devicemanagement.builder;

import com.example.devicemanagement.dtos.DeviceDTO;
import com.example.devicemanagement.entities.Device;

public class DeviceBuilder {
    public static DeviceDTO convertEntityToDTO(Device device){
        return DeviceDTO.builder()
                .id(device.getId())
                .description(device.getDescription())
                .maxHourlyEnergyConsumption(device.getMaxHourlyEnergyConsumption())
                .personDTO(PersonBuilder.convertEntityToDTO(device.getUser()))
                .build();
    }

    public static Device convertDtoToEntity(DeviceDTO deviceDTO){
        return new Device(deviceDTO.getDescription(), deviceDTO.getMaxHourlyEnergyConsumption(),PersonBuilder.convertDtoToEntity(deviceDTO.getPersonDTO()));
    }
}
