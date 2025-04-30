package com.example.movieticketbookingsystem.entity;

import com.example.movieticketbookingsystem.enums.ScreenType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String screenId;

    @Enumerated(EnumType.STRING)
    private ScreenType screenType;

    private Integer capacity;
    private Integer noOfRows;

    @Column(name = "created_At", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_At", nullable = false)
    @LastModifiedDate
    private Instant updatedAt;

    @Column(name = "created_By", nullable = false, updatable = false)
    @CreatedBy
    private String createdBy;

    @Column(name = "updated_By", nullable = false)
    @LastModifiedBy
    private String updatedBy;


    @ManyToOne
    private Theater theater;

    @OneToMany(mappedBy = "screen",fetch = FetchType.EAGER)
    private List<Seat> seat = new ArrayList<>();

}
