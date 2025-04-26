package com.example.movieticketbookingsystem.entity;

import com.example.movieticketbookingsystem.enums.screenType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Screen {

    @Id
    private String screenId;

    @Enumerated(EnumType.STRING)
    private screenType screenType;

    private Integer capacity;
    private Long createdAt;
    private Long updatedAt;
    private String createdBy;

}
