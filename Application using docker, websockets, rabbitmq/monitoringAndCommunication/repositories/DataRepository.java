package com.example.monitoringAndCommunication.repositories;

import com.example.monitoringAndCommunication.entities.Data;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DataRepository  extends CrudRepository<Data, UUID> {
}
