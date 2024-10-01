package com.example.devicemanagement.controllers;

import com.example.devicemanagement.dtos.DeviceDTO;
import com.example.devicemanagement.services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/device")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @RequestMapping(value = "/getAll",method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().allow(HttpMethod.GET).build();
    }
    @GetMapping(value = "/getAll")
    public ResponseEntity<List<DeviceDTO>> getDevices(){
        List<DeviceDTO> dtos = deviceService.findDevices();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping(value = "/insertDevice")
    public ResponseEntity<UUID> insertDevice(@Valid @RequestBody DeviceDTO dto){
        UUID id = deviceService.insertDevice(dto);
        return new ResponseEntity<>(id,HttpStatus.OK);
    }

    @GetMapping(value = "/getById/{id}")
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable("id") UUID id){
        DeviceDTO dto = deviceService.findDeviceById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping(value = "/updateDevice")
    @ResponseBody
    public ResponseEntity<DeviceDTO> updateDevice(@RequestBody DeviceDTO dto){
        DeviceDTO deviceDTO = deviceService.updateDevice(dto);
        return new ResponseEntity<>(deviceDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteById/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteDevice(@PathVariable UUID id){
        deviceService.deleteDevice(id);
        return ResponseEntity.ok("{\"message\": \"Success\"}");
    }

}
