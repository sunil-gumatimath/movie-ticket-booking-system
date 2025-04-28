package com.example.movieticketbookingsystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, TestFilter testFilter) throws Exception {
        // disabling csrf protection
        http.csrf(csrf -> csrf.disable());

        // specify public and private routes
        http.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/register")
                .permitAll().anyRequest().authenticated());

        http.addFilterAfter(testFilter, UsernamePasswordAuthenticationFilter.class);

        // type of authentication to be done (form login)
        http.formLogin(Customizer.withDefaults());


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
