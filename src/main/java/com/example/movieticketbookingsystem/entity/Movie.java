package com.example.movieticketbookingsystem.entity;

import com.example.movieticketbookingsystem.enums.Certificate;
import com.example.movieticketbookingsystem.enums.Genre;
import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Movie {

    @Id
    @Column(name="movie_id")
    @GeneratedValue(strategy= GenerationType.UUID)
    private String movieId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ElementCollection
    private Set<String> castList;

    @Column(name = "runtime")
    private Duration runtime;

    @Enumerated(EnumType.STRING)
    @Column(name = "certificate")
    private Certificate certificate;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Genre genre;

    @OneToMany(mappedBy = "movie")
    private List<Feedback> feedbacks;
}
