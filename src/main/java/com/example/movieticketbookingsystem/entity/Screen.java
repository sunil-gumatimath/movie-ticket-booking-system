package com.example.movieticketbookingsystem.entity;

import com.example.movieticketbookingsystem.enums.ScreenType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String screenId;

    @Enumerated(EnumType.STRING)
    private ScreenType screenType;

    private Integer capacity;
    private Integer noOfRows;

    private Long createdAt;
    private Long updatedAt;
    private String createdBy;

    @ManyToOne
    private Theater theater;

    @OneToMany(mappedBy = "screen",fetch = FetchType.EAGER)
    private List<Seat> seat = new ArrayList<>();

}
