package com.Megaminds.Recrutement.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private LocalDate signedDate;

    @OneToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;
}

