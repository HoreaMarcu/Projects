package com.example.devicemanagement.builder;

import com.example.devicemanagement.dtos.PersonDTO;
import com.example.devicemanagement.entities.Person;

public class PersonBuilder {

    public static PersonDTO convertEntityToDTO(Person person){
        return PersonDTO.builder()
                .id(person.getId())
                .name(person.getName())
                .password(person.getPassword())
                .age(person.getAge())
                .adminRole(person.isAdminRole())
                .build();
    }

    public static Person convertDtoToEntity(PersonDTO personDTO){
        return new Person(personDTO.getId(),
                personDTO.getName(),
                personDTO.getPassword(),
                personDTO.getAge(),
                personDTO.isAdminRole());
    }
}
