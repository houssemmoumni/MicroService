package com.Megaminds.Recrutement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // Type de notification (ex: "success", "info", "warning")
    private String message; // Message de la notification

    private boolean isRead; // Indique si la notification a été lue

    private LocalDateTime createdAt = LocalDateTime.now(); // Date de création

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false) // Relation avec Application
    private Application application; // Lien vers la candidature associée

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

    public boolean isRead() { // Getter for isRead
        return isRead;
    }

    public void setRead(boolean read) { // Setter for isRead
        isRead = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
}