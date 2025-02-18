package com.Megaminds.Recrutement.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "recruiter")
    private List<JobOffer> jobOffers;
}
