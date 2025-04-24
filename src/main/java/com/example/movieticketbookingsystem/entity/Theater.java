package com.example.movieticketbookingsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Theater {

    @Id
    private String theaterId;
    private String name;
    private String address;
    private String city;
    private String landmark;
    private Long createdAt;
    private String updatedAt;
    private String createdBy;
}
