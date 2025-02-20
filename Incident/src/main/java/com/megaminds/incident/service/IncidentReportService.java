package com.megaminds.incident.service;

import com.megaminds.incident.entity.IncidentReport;
import com.megaminds.incident.repository.IncidentReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncidentReportService {
    private final IncidentReportRepository incidentReportRepository;

    public IncidentReportService(IncidentReportRepository incidentReportRepository) {
        this.incidentReportRepository = incidentReportRepository;
    }

    public IncidentReport createIncident(IncidentReport incidentReport) {
        return incidentReportRepository.save(incidentReport);
    }

    public List<IncidentReport> getAllIncidents() {
        return incidentReportRepository.findAll();
    }

    public Optional<IncidentReport> getIncidentById(Long id) {
        return incidentReportRepository.findById(id);
    }
}
