package com.megaminds.incident.dto;

import com.megaminds.incident.entity.IncidentSeverity;
import com.megaminds.incident.entity.IncidentStatus;
import java.time.LocalDate;

public class IncidentReportDTO {
    private String description;
    private LocalDate reportDate;
    private IncidentStatus status;
    private IncidentSeverity severity;
    private Long reportedById;
    private Long assignedToId;
    private Long projectId;
    private String reporterName; // Added field

    // Getters and Setters
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getReportDate() { return reportDate; }
    public void setReportDate(LocalDate reportDate) { this.reportDate = reportDate; }
    public IncidentStatus getStatus() { return status; }
    public void setStatus(IncidentStatus status) { this.status = status; }
    public IncidentSeverity getSeverity() { return severity; }
    public void setSeverity(IncidentSeverity severity) { this.severity = severity; }
    public Long getReportedById() { return reportedById; }
    public void setReportedById(Long reportedById) { this.reportedById = reportedById; }
    public Long getAssignedToId() { return assignedToId; }
    public void setAssignedToId(Long assignedToId) { this.assignedToId = assignedToId; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getReporterName() { return reporterName; } // Added getter
    public void setReporterName(String reporterName) { this.reporterName = reporterName; } // Added setter
}