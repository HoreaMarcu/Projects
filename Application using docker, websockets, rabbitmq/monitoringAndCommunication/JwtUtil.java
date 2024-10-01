package com.example.monitoringAndCommunication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    private byte[] SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();

    private final RestTemplate restTemplate;

    public JwtUtil() {
        this.restTemplate = new RestTemplate();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
//        if (token != null) {
//            String localhost8080Url = "http://localhost:8080/isTokenValid";
        String localhost8080Url = "http://firstApp:8080/isTokenValid"; //localhost
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("jwt", token);
            System.out.println("token: \n" + token);


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            System.out.println("REQUEST BODY:");
            System.out.println(requestBody);
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Boolean> response = restTemplate.postForEntity(localhost8080Url, requestEntity, Boolean.class);
            return response.getBody();
//        }
//        return true;
    }
}