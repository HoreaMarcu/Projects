package com.example.devicemanagement;

import com.example.devicemanagement.entities.Person;
import com.example.devicemanagement.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    PersonRepository personRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByName(username);
        System.out.println("PERSON: ");
        System.out.println(person);
//        if(person == null){
//            throw new UsernameNotFoundException("User not found");
//        }
        return new User("foo","foo", new ArrayList<>());
//        return new User("foo", "foo",
//                new ArrayList<>());
    }
}
