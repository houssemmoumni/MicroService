package com.megaminds.incident.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
public class IncidentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private LocalDate reportDate;
    private String resolutionToken;

    @Enumerated(EnumType.STRING)
    private IncidentStatus status;

    @Enumerated(EnumType.STRING)
    private IncidentSeverity severity;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "reported_by")
    private User reportedBy;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "incidentReport", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<IncidentAction> actions;

    // Constructors
    public IncidentReport() {}

    public IncidentReport(String description, LocalDate reportDate, IncidentStatus status,
                          IncidentSeverity severity, User reportedBy, User assignedTo,
                          Project project) {
        this.description = description;
        this.reportDate = reportDate;
        this.status = status;
        this.severity = severity;
        this.reportedBy = reportedBy;
        this.assignedTo = assignedTo;
        this.project = project;
        this.resolutionToken = UUID.randomUUID().toString();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getReportDate() { return reportDate; }
    public void setReportDate(LocalDate reportDate) { this.reportDate = reportDate; }
    public IncidentStatus getStatus() { return status; }
    public void setStatus(IncidentStatus status) { this.status = status; }
    public IncidentSeverity getSeverity() { return severity; }
    public void setSeverity(IncidentSeverity severity) { this.severity = severity; }
    public User getReportedBy() { return reportedBy; }
    public void setReportedBy(User reportedBy) { this.reportedBy = reportedBy; }
    public User getAssignedTo() { return assignedTo; }
    public void setAssignedTo(User assignedTo) { this.assignedTo = assignedTo; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public String getResolutionToken() { return resolutionToken; }
    public void setResolutionToken(String resolutionToken) { this.resolutionToken = resolutionToken; }
    public List<IncidentAction> getActions() { return actions; }
    public void setActions(List<IncidentAction> actions) { this.actions = actions; }

    @PrePersist
    public void generateToken() {
        if (this.resolutionToken == null) {
            this.resolutionToken = UUID.randomUUID().toString();
        }
    }
}