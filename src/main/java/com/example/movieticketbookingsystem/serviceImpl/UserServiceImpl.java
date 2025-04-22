package com.example.movieticketbookingsystem.serviceImpl;

import com.example.movieticketbookingsystem.entity.TheaterOwner;
import com.example.movieticketbookingsystem.entity.User;
import com.example.movieticketbookingsystem.entity.UserDetails;
import com.example.movieticketbookingsystem.enums.UserRole;
import com.example.movieticketbookingsystem.exception.UserExistByEmailException;
import com.example.movieticketbookingsystem.repository.UserRepository;
import com.example.movieticketbookingsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public UserDetails addUserDetails(UserDetails userDetails) {
        if (userRepository.existsByEmail(userDetails.getEmail())){
            throw new UserExistByEmailException("User with Email is already exists");
        }else if (userDetails.getUserRole() == UserRole.USER){
            User user = new User();

            user.setUserId(userDetails.getUserId());
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            user.setUserRole(userDetails.getUserRole());
            user.setPhoneNumber(userDetails.getPhoneNumber());
            user.setDateOfBirth(userDetails.getDateOfBirth());
            user.setCreatedAt(userDetails.getCreatedAt());
            user.setUpdatedAt(userDetails.getUpdatedAt());

            return userRepository.save(user);

        }else{
            TheaterOwner theaterOwner = new TheaterOwner();


            theaterOwner.setUserId(userDetails.getUserId());
            theaterOwner.setUsername(userDetails.getUsername());
            theaterOwner.setEmail(userDetails.getEmail());
            theaterOwner.setPassword(userDetails.getPassword());
            theaterOwner.setUserRole(userDetails.getUserRole());
            theaterOwner.setPhoneNumber(userDetails.getPhoneNumber());
            theaterOwner.setDateOfBirth(userDetails.getDateOfBirth());
            theaterOwner.setCreatedAt(userDetails.getCreatedAt());
            theaterOwner.setUpdatedAt(userDetails.getUpdatedAt());

            return userRepository.save(theaterOwner);

        }
    }
}
