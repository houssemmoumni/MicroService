package com.megaminds.incident.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    private String description;
    private boolean published = false;

    @Lob
    @Column(length = 1048576)
    private String image; // Une seule image maintenant

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<IncidentReport> incidents;

    public Project() {}

    public Project(Long id) {
        this.id = id;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isPublished() { return published; }
    public void setPublished(boolean published) { this.published = published; }

    public List<IncidentReport> getIncidents() { return incidents; }
    public void setIncidents(List<IncidentReport> incidents) { this.incidents = incidents; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}