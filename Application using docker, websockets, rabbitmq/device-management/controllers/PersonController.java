package com.example.devicemanagement.controllers;

import com.example.devicemanagement.dtos.PersonDTO;
import com.example.devicemanagement.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE).build();
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<PersonDTO>> getPersons() {
        List<PersonDTO> dtos = personService.findPersons();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
    @RequestMapping(value="/insertPerson", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions2() {
        return ResponseEntity.ok().allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE).build();
    }
    @PostMapping("/insertPerson")
    public ResponseEntity<UUID> insertPerson(@Valid @RequestBody PersonDTO personDTO) {
        UUID personID = personService.insert(personDTO);
        return new ResponseEntity<>(personID, HttpStatus.CREATED);
    }

    @GetMapping(value = "/getPersonById/{id}")
    public ResponseEntity<PersonDTO> getPerson(@PathVariable("id") UUID personId) {
        PersonDTO dto = personService.findPersonById(personId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/updatePerson")
    @ResponseBody
    public ResponseEntity<PersonDTO> updatePerson(@RequestBody PersonDTO personDetailsDTO){
        PersonDTO dto = personService.updatePerson(personDetailsDTO);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deletePersonById/{id}")
    @ResponseBody
    public ResponseEntity<String> deletePerson(@PathVariable UUID id) {
        String result = personService.deletePerson(id);
        return ResponseEntity.ok("{\"message\": \"Success\"}");
    }

}
