package com.example.movieticketbookingsystem.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class TheaterOwner extends UserDetails{

    @OneToMany(mappedBy = "owner",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Theater> theaters = new ArrayList<>();
}
