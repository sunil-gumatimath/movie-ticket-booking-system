package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.repository.UserRepository;
import com.example.movieticketbookingsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
}
