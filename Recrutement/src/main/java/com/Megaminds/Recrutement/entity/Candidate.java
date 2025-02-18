package com.Megaminds.Recrutement.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity

public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    @Lob
    private byte[] resume; // Store the resume as a binary file

    @OneToMany(mappedBy = "candidate")
    private List<Application> applications;
}
