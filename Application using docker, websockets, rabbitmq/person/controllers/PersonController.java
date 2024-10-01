package ro.tuc.ds2020.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.JwtUtil;
import ro.tuc.ds2020.MyUserDetailsService;
import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.dtos.PersonDetailsDTO;
import ro.tuc.ds2020.dtos.builders.PersonBuilder;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.repositories.PersonRepository;
import ro.tuc.ds2020.services.PersonService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/person")
public class PersonController {

    private final PersonService personService;
    private final RestTemplate restTemplate;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;


    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
        this.restTemplate = new RestTemplate();
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE).build();
    }

    @GetMapping()
    public ResponseEntity<List<PersonDTO>> getPersons() {
        System.out.println("CALL LA GET PERSONS");
        List<PersonDTO> dtos = personService.findPersons();
        for (PersonDTO dto : dtos) {
            Link personLink = linkTo(methodOn(PersonController.class)
                    .getPerson(dto.getId())).withRel("personDetails");
            dto.add(personLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @RequestMapping(value="/insertPerson", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions2() {
        return ResponseEntity.ok().allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE).build();
    }
    @PostMapping("/insertPerson")
    public ResponseEntity<UUID> insertProsumer(@Valid @RequestBody PersonDetailsDTO personDTO) throws JsonProcessingException {
        UUID personID = personService.insert(personDTO);
        personDTO.setId(personID);
//        String localhost8081Url = "http://localhost:8081/person/insertPerson";
        String localhost8081Url = "http://device:8081/person/insertPerson";
        String personData = "{ \"id\": \"" + personID + "\", \"name\": \"" + personDTO.getName() + "\", \"password\": \"" + personDTO.getPassword() + "\", \"age\": " + personDTO.getAge() + ", \"adminRole\": \"" + personDTO.isAdminRole() + "\" }";



        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

//        final UserDetails userDetails = userDetailsService
//                .loadUserByUsername(personDTO.getName());
//
//        final String jwt = jwtTokenUtil.generateToken(userDetails);
//
//        headers.setBearerAuth(jwt);

        String response = restTemplate.postForObject(
                localhost8081Url,
                new HttpEntity<>(personData, headers),
                String.class
        );

        return new ResponseEntity<>(personID, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PersonDetailsDTO> getPerson(@PathVariable("id") UUID personId) {
        PersonDetailsDTO dto = personService.findPersonById(personId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //TODO: UPDATE, DELETE per resource
    @PutMapping()
    @ResponseBody
    public ResponseEntity<PersonDetailsDTO> updatePerson(@RequestBody PersonDetailsDTO personDetailsDTO) {
        PersonDetailsDTO dto = personService.updatePerson(personDetailsDTO);

//        String localhost8081Url = "http://localhost:8081/person/updatePerson";
        String localhost8081Url = "http://device:8081/person/updatePerson";
        String personData = "{ \"id\": \"" + personDetailsDTO.getId() + "\", \"name\": \"" + personDetailsDTO.getName() + "\", \"age\": " + personDetailsDTO.getAge() + " }";


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = restTemplate.exchange(
                localhost8081Url,
                HttpMethod.PUT,
                new HttpEntity<>(personData, headers),
                String.class
        );

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<String> deletePerson(@PathVariable UUID id) {
        String result = personService.deletePerson(id);

//        String localhost8081Url = "http://localhost:8081/person/deletePersonById/" + id;
        String localhost8081Url = "http://device:8081/person/deletePersonById/" + id;


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        PersonDTO personDTO = personService.findPersons().get(0);
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(personDTO.getName());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        headers.setBearerAuth(jwt);

        ResponseEntity<String> response = restTemplate.exchange(
                localhost8081Url,
                HttpMethod.DELETE,
                new HttpEntity<>("", headers),
                String.class
        );

        return ResponseEntity.ok("{\"message\": \"Success\"}");
    }

    @GetMapping(value = "getPersonByToken/{token}")
    public ResponseEntity<PersonDetailsDTO> getPerson(@PathVariable("token") String token) {
        Person person = personService.getPersonByToken(token);
        return new ResponseEntity<>(PersonBuilder.toPersonDetailsDTO(person), HttpStatus.OK);
    }
}
