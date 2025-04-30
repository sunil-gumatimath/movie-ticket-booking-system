package com.example.movieticketbookingsystem.entity;

import com.example.movieticketbookingsystem.enums.Certificate;
import com.example.movieticketbookingsystem.enums.Genre;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Movie {

    @Id
    private String movieId;
    private String title;
    private String description;
    private String[] cast;

    private Duration runtime;
    private Certificate certificate;
    private Genre genre;
}
