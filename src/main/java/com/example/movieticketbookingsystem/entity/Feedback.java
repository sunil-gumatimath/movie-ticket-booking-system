package com.example.movieticketbookingsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = Feedback.USER_MOVIE_UNIQUE_CONSTRAINT, columnNames = {"user_id", "movie_id"}))
@EntityListeners(AuditingEntityListener.class)
public class Feedback {

    public static final String USER_MOVIE_UNIQUE_CONSTRAINT = "uk_feedback_user_movie";

    @Id
    @Column(name = "feedback_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String feedbackId;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "review", length = 500, nullable = false)
    @NotBlank
    @Size(max = 500)
    private String review;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    @CreatedBy
    private String createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
