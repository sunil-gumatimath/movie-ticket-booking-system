package com.example.movieticketbookingsystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RegisterUserTest {

    @Test
    public void registerPriorityUser() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/register";

        String json = "{"
                + "\"username\": \"priority_user_test\","
                + "\"email\": \"priority_test@gmail.com\","
                + "\"phoneNumber\": \"9876543210\","
                + "\"password\": \"Password123!\","
                + "\"dateOfBirth\": \"1995-01-01\","
                + "\"userRole\": \"ROLE_USER\""
                + "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        try {
            String response = restTemplate.postForObject(url, entity, String.class);
            System.out.println("Registration Successful: " + response);
        } catch (Exception e) {
            System.err.println("Registration Failed: " + e.getMessage());
        }
    }
}
