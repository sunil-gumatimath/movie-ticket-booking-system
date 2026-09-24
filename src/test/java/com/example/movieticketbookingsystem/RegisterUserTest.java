package com.example.movieticketbookingsystem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "jwt.secret=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
)
class RegisterUserTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void registersNormalUser() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String json = "{"
                + "\"username\": \"test_user_" + suffix + "\","
                + "\"email\": \"test_" + suffix + "@gmail.com\","
                + "\"phoneNumber\": \"9876543210\","
                + "\"password\": \"Password123!\","
                + "\"dateOfBirth\": \"1995-01-01\""
                + "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/register", new HttpEntity<>(json, headers), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("test_" + suffix + "@gmail.com");
    }
}
