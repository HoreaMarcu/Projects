package com.example.monitoringAndCommunication.builders;

import com.example.monitoringAndCommunication.dtos.DataDTO;
import com.example.monitoringAndCommunication.entities.Data;

public class DataBuilder {
    public static DataDTO convertEntityToDTO(Data data){
        return DataDTO.builder()
                .device_id(data.getDevice_id())
                .measurement_value(data.getMeasurement_value())
                .timestamp(data.getTimestamp())
                .build();
    }

    public static Data convertDtoToEntity(DataDTO dataDTO){
        return new Data(dataDTO.getTimestamp(),dataDTO.getDevice_id(),dataDTO.getMeasurement_value());
    }
}
