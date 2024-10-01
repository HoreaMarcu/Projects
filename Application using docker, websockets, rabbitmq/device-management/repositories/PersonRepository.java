package com.example.devicemanagement.repositories;

import com.example.devicemanagement.entities.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonRepository extends CrudRepository<Person, UUID> {

    Person findByName(String name);

}
