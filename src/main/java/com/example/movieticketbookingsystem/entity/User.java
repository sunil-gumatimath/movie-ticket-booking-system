package com.example.movieticketbookingsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends UserDetails{

    @OneToMany(mappedBy = "user")
    private List<Feedback> feedbacks;

}
