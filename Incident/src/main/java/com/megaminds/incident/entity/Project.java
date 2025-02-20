package com.megaminds.incident.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private String description;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<IncidentReport> incidents; // Liste des incidents liés à ce projet

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<IncidentReport> getIncidents() {
        return incidents;
    }

    public void setIncidents(List<IncidentReport> incidents) {
        this.incidents = incidents;
    }
}
