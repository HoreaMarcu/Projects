package com.example.devicemanagement.services;

import com.example.devicemanagement.builder.PersonBuilder;
import com.example.devicemanagement.dtos.PersonDTO;
import com.example.devicemanagement.entities.Person;
import com.example.devicemanagement.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public List<PersonDTO> findPersons() {
        List<Person> personList = (List<Person>) personRepository.findAll();
        return personList.stream()
                .map(PersonBuilder::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findPersonById(UUID id) {
        Optional<Person> prosumerOptional = personRepository.findById(id);
        if (prosumerOptional.isEmpty()) {
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }
        return PersonBuilder.convertEntityToDTO(prosumerOptional.get());
    }

    public UUID insert(PersonDTO personDTO) {
        Person person = PersonBuilder.convertDtoToEntity(personDTO);
        person = personRepository.save(person);
        return person.getId();
    }

    public PersonDTO updatePerson(PersonDTO personDTO){
        if(personRepository.findById(personDTO.getId()).isPresent()) {
            Person person = personRepository.findById(personDTO.getId()).get();
            person.setName(personDTO.getName());
            person.setAge(person.getAge());
            person.setAdminRole(personDTO.isAdminRole());
            personRepository.save(person);
            return personDTO;
        }
        return null;
    }

    public String deletePerson(UUID id){
        if(personRepository.findById(id).isPresent()) {
            Person currentPerson = personRepository.findById(id).get();
            personRepository.delete(currentPerson);
            return "SUCCESS";
        }
        return "PERSON NOT FOUND";
    }
}
