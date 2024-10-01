package com.example.monitoringAndCommunication.services;

import com.example.monitoringAndCommunication.builders.DataBuilder;
import com.example.monitoringAndCommunication.dtos.DataDTO;
import com.example.monitoringAndCommunication.entities.Data;
import com.example.monitoringAndCommunication.repositories.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DataService {

    @Autowired
    private DataRepository dataRepository;

    public List<DataDTO> findAllData() {
        List<Data> dataList = (List<Data>) dataRepository.findAll();

        return dataList.stream()
                .map(DataBuilder::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    public UUID insertData(DataDTO dto) {
        Data data = DataBuilder.convertDtoToEntity(dto);;
        data = dataRepository.save(data);
        return data.getId();
    }

    public DataDTO findDataById(UUID id) {
        Optional<Data> dataOptional = dataRepository.findById(id);
        if(dataOptional.isEmpty()){
            throw new ResourceNotFoundException(Data.class.getSimpleName() + " with id: " + id);
        }
        return DataBuilder.convertEntityToDTO(dataOptional.get());
    }


    public String deleteData(UUID id) {
        if(dataRepository.findById(id).isPresent()){
            Data data = dataRepository.findById(id).get();
            dataRepository.delete(data);
            return "SUCCESS";
        }
        return "DEVICE NOT FOUND";

    }
}
