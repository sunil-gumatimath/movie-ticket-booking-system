package com.example.movieticketbookingsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Feedback {

    @Id
    @Column(name = "feedback_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String feedbackId;

    @Column(name = "rating")
    private int rating;

    @Column(name = "review",length = 500)
    @NotBlank
    private String review;

    @Column(name = "created_By", nullable = false, updatable = false)
    @CreatedBy
    private Instant createdAt;

    @ManyToOne
    @Column(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @Column(name = "user_id",nullable = false)
    private User user;

}
