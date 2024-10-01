package com.example.monitoringAndCommunication.controllers;

import com.example.monitoringAndCommunication.dtos.DataDTO;
import com.example.monitoringAndCommunication.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/monitoring")
public class DataController {

    @Autowired
    private DataService dataService;

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<DataDTO>> getAllData(){
        List<DataDTO> dtos =dataService.findAllData();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping(value = "/insertData")
    public ResponseEntity<UUID> insertData(@Valid @RequestBody DataDTO dto){
        UUID id = dataService.insertData(dto);
        return new ResponseEntity<>(id,HttpStatus.OK);
    }

    @GetMapping(value = "/getById/{id}")
    public ResponseEntity<DataDTO> getData(@PathVariable("id") UUID id){
        DataDTO dto = dataService.findDataById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteById/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteData(@PathVariable UUID id){
        dataService.deleteData(id);
        return ResponseEntity.ok("{\"message\": \"Success\"}");
    }
}
