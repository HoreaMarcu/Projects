package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ro.tuc.ds2020.AuthenticationRequest;
import ro.tuc.ds2020.AuthenticationResponse;
import ro.tuc.ds2020.JwtUtil;
import ro.tuc.ds2020.MyUserDetailsService;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.repositories.PersonRepository;
import ro.tuc.ds2020.services.PersonService;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    private final RestTemplate restTemplate;

    @Autowired
    private PersonRepository personRepository;
    public AuthenticationController() {
        this.restTemplate = new RestTemplate();
    }
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        System.out.println("in prima functie");
        try {
            System.out.println("Before authenticationManager.authenticate");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
            System.out.println("a mers ce e in try");
        } catch (BadCredentialsException e) {
            System.out.println("BadCredentialsException: Incorrect username or password");
            throw new Exception("Incorrect username or password", e);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            throw e;
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        Person person = personRepository.findByName(authenticationRequest.getUsername());
        person.setToken(jwt);
        personRepository.save(person);



//        new AuthenticationResponse(jwt);
//        String localhost8081Url = "http://localhost:8081/authenticate";
////        String localhost8081Url = "http://device:8081/authenticate";
////        String personData = "{ \"jwt\": \"" + jwt + "\" }";
//        Map<String, String> requestBody = new HashMap<>();
//        requestBody.put("jwt", jwt);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
//        ResponseEntity<Boolean> response = restTemplate.postForEntity(localhost8081Url, requestEntity, Boolean.class);

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        System.out.println("DA CALL LA DEVICE : " + localhost8081Url);
//        ResponseEntity<String> response = restTemplate.exchange(
//                localhost8081Url,
//                HttpMethod.POST,
//                new HttpEntity<>(personData, headers),
//                String.class
//        );

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @RequestMapping(value = "/isTokenValid", method = RequestMethod.POST)
    public ResponseEntity<?> isTokenValid(@RequestBody Map<String, String> requestBody) {
        String jwt = requestBody.get("jwt");
        System.out.println("RECEIVED JWT:  " + jwt);
        boolean result = jwtTokenUtil.validateToken2(new AuthenticationResponse(jwt));
        return ResponseEntity.ok(result);
    }

}
