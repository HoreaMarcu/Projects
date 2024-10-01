package com.example.monitoringAndCommunication.controllers;

import com.example.monitoringAndCommunication.AuthenticationRequest;
import com.example.monitoringAndCommunication.AuthenticationResponse;
import com.example.monitoringAndCommunication.JwtUtil;
import com.example.monitoringAndCommunication.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<Boolean> createAuthenticationToken(@RequestBody Map<String, String> requestBody){
        String jwt = requestBody.get("jwt");
        System.out.println("in prima functie");
        new AuthenticationResponse(jwt);
        return ResponseEntity.ok(true);
    }

}