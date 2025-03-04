package com.Megaminds.Recrutement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // Type de notification (ex: "success", "error")
    private String message; // Message de la notification

    @Column(name = "is_read") // Renommez la colonne en "is_read"
    private boolean read; // Indique si la notification a été lue

    @ManyToOne
    @JoinColumn(name = "interview_id")
    private Interview interview; // Lien vers l'entretien associé

    private LocalDateTime createdAt = LocalDateTime.now(); // Date de création

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Interview getInterview() {
        return interview;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}