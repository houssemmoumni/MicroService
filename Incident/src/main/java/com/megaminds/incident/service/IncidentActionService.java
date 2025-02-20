package com.megaminds.incident.service;

import com.megaminds.incident.entity.IncidentAction;
import com.megaminds.incident.repository.IncidentActionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentActionService {
    private final IncidentActionRepository incidentActionRepository;

    public IncidentActionService(IncidentActionRepository incidentActionRepository) {
        this.incidentActionRepository = incidentActionRepository;
    }

    public IncidentAction addAction(IncidentAction action) {
        return incidentActionRepository.save(action);
    }

    public List<IncidentAction> getAllActions() {
        return incidentActionRepository.findAll();
    }
}
