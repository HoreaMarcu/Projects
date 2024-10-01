package com.example.monitoringAndCommunication;

import com.example.monitoringAndCommunication.dtos.DataDTO;
import com.example.monitoringAndCommunication.services.DataService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;


@Component
public class MessageListener {

    @Autowired
    private DataService dataService;


    @RabbitListener(queues = "rabbitQueue")
    public void listener(MessageRabbitMQ message) throws IOException {
        DataDTO dataDTO = DataDTO.builder()
                .timestamp(message.getTimestamp())
                .device_id(message.getDevice_id())
                .measurement_value(message.getMeasurement_value())
                .build();
        dataService.insertData(dataDTO);
        System.out.println(message);

        ArrayList<DataDTO> dtos = (ArrayList<DataDTO>) dataService.findAllData();
        double sum = 0;
        for(DataDTO dto:dtos){
            if(dto.getDevice_id().equals(message.getDevice_id()))
                sum+= Double.parseDouble(dto.getMeasurement_value());
        }
        if(sum > message.getMaxHourlyEnergyConsumption())
            HandlerWebSocket.sendMessageToAll(message.toString());

    }

}