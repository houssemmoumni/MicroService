package com.megaminds.incident.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class IncidentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private LocalDate reportDate;

    @Enumerated(EnumType.STRING)
    private IncidentStatus status; // DECLARED, ASSIGNED, RESOLVED, REOPENED

    @Enumerated(EnumType.STRING)
    private IncidentSeverity severity; // LOW, MEDIUM, HIGH

    @ManyToOne
    @JoinColumn(name = "reported_by")
    private User reportedBy; // L'ouvrier qui a déclaré l'incident

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo; // Le technicien assigné par l'admin

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project; // Le projet dans lequel l'incident est déclaré

    @OneToMany(mappedBy = "incidentReport", cascade = CascadeType.ALL)
    private List<IncidentAction> actions; // Actions effectuées sur cet incident


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public void setStatus(IncidentStatus status) {
        this.status = status;
    }

    public IncidentSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(IncidentSeverity severity) {
        this.severity = severity;
    }

    public User getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(User reportedBy) {
        this.reportedBy = reportedBy;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<IncidentAction> getActions() {
        return actions;
    }

    public void setActions(List<IncidentAction> actions) {
        this.actions = actions;
    }
}
