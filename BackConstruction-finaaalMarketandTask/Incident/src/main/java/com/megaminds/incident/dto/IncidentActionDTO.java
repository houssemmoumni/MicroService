package com.megaminds.incident.dto;

import com.megaminds.incident.entity.IncidentActionType;
import java.time.LocalDate;

public class IncidentActionDTO {
    private String description;
    private LocalDate actionDate;
    private IncidentActionType actionType;
    private Long incidentReportId;
    private Long performedById;

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

    public Long getIncidentReportId() {
        return incidentReportId;
    }

    public void setIncidentReportId(Long incidentReportId) {
        this.incidentReportId = incidentReportId;
    }

    public Long getPerformedById() {
        return performedById;
    }

    public void setPerformedById(Long performedById) {
        this.performedById = performedById;
    }
}
