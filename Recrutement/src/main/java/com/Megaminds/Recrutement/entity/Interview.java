package com.Megaminds.Recrutement.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate interviewDate;
    private String feedback;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;
}

