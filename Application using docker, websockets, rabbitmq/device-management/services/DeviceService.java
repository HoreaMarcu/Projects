package com.example.devicemanagement.services;

import com.example.devicemanagement.builder.DeviceBuilder;
import com.example.devicemanagement.dtos.DeviceDTO;
import com.example.devicemanagement.entities.Device;
import com.example.devicemanagement.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    public List<DeviceDTO> findDevices(){
        List<Device> deviceList = (List<Device>) deviceRepository.findAll();
        return deviceList.stream()
                .map(DeviceBuilder::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    public DeviceDTO findDeviceById(UUID id){
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if(deviceOptional.isEmpty()){
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return DeviceBuilder.convertEntityToDTO(deviceOptional.get());
    }

    public UUID insertDevice(DeviceDTO deviceDTO){
        Device device = DeviceBuilder.convertDtoToEntity(deviceDTO);;
        device = deviceRepository.save(device);
        return device.getId();
    }

    public DeviceDTO updateDevice(DeviceDTO deviceDTO){
        if(deviceRepository.findById(deviceDTO.getId()).isPresent()){
            Device device = deviceRepository.findById(deviceDTO.getId()).get();
            device.setDescription(deviceDTO.getDescription());
            device.setMaxHourlyEnergyConsumption(deviceDTO.getMaxHourlyEnergyConsumption());
            deviceRepository.save(device);
            return deviceDTO;
        }
        return null;
    }

    public String deleteDevice(UUID id){
        if(deviceRepository.findById(id).isPresent()){
            Device device = deviceRepository.findById(id).get();
            deviceRepository.delete(device);
            return "SUCCESS";
        }
        return "DEVICE NOT FOUND";
    }

}
