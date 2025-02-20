package com.megaminds.incident.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class IncidentAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private LocalDate actionDate;

    @Enumerated(EnumType.STRING)
    private IncidentActionType actionType; // CREATED, ASSIGNED, RESOLVED, REOPENED

    @ManyToOne
    @JoinColumn(name = "incident_id")
    private IncidentReport incidentReport; // Incident lié à cette action

    @ManyToOne
    @JoinColumn(name = "performed_by")
    private User performedBy; // Le technicien ou admin qui a réalisé l'action

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

    public LocalDate getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDate actionDate) {
        this.actionDate = actionDate;
    }

    public IncidentActionType getActionType() {
        return actionType;
    }

    public void setActionType(IncidentActionType actionType) {
        this.actionType = actionType;
    }

    public IncidentReport getIncidentReport() {
        return incidentReport;
    }

    public void setIncidentReport(IncidentReport incidentReport) {
        this.incidentReport = incidentReport;
    }

    public User getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(User performedBy) {
        this.performedBy = performedBy;
    }
}
