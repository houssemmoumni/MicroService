package com.megaminds.incident.controllers;

import com.megaminds.incident.dto.IncidentReportDTO;
import com.megaminds.incident.entity.*;
import com.megaminds.incident.service.IncidentReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@CrossOrigin(origins = "http://localhost:4200")
public class IncidentReportController {
    private final IncidentReportService incidentReportService;

    public IncidentReportController(IncidentReportService incidentReportService) {
        this.incidentReportService = incidentReportService;
    }

    @PostMapping
    public ResponseEntity<IncidentReport> createIncident(@RequestBody IncidentReportDTO incidentReportDTO) {
        IncidentReport incidentReport = convertToEntity(incidentReportDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(incidentReportService.createIncident(incidentReport));
    }

    @GetMapping
    public ResponseEntity<List<IncidentReport>> getAllIncidents() {
        return ResponseEntity.ok(incidentReportService.getAllIncidents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentReport> getIncidentById(@PathVariable Long id) {
        return incidentReportService.getIncidentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<IncidentReport>> getIncidentsByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(incidentReportService.getIncidentsByProject(projectId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<IncidentReport>> getIncidentsByStatus(@PathVariable IncidentStatus status) {
        return ResponseEntity.ok(incidentReportService.getIncidentsByStatus(status));
    }

    @PatchMapping("/{incidentId}/assign/{technicianId}")
    public ResponseEntity<IncidentReport> assignTechnician(
            @PathVariable Long incidentId,
            @PathVariable Long technicianId) {
        return ResponseEntity.ok(incidentReportService.assignIncident(incidentId, technicianId));
    }

    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<IncidentReport> updateStatus(
            @PathVariable Long id,
            @PathVariable IncidentStatus status) {
        return incidentReportService.getIncidentById(id)
                .map(incident -> {
                    incident.setStatus(status);
                    return ResponseEntity.ok(incidentReportService.createIncident(incident));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private IncidentReport convertToEntity(IncidentReportDTO dto) {
        IncidentReport incidentReport = new IncidentReport();
        incidentReport.setDescription(dto.getDescription());
        incidentReport.setReportDate(dto.getReportDate());
        incidentReport.setStatus(dto.getStatus() != null ? dto.getStatus() : IncidentStatus.DECLARED);
        incidentReport.setSeverity(dto.getSeverity() != null ? dto.getSeverity() : IncidentSeverity.MEDIUM);

        if (dto.getReportedById() != null) {
            incidentReport.setReportedBy(new User(dto.getReportedById()));
        }
        if (dto.getProjectId() != null) {
            incidentReport.setProject(new Project(dto.getProjectId()));
        }

        return incidentReport;
    }
}