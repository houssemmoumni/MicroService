package com.megaminds.incident.controllers;

import com.megaminds.incident.entity.IncidentAction;
import com.megaminds.incident.service.IncidentActionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incident-actions")
public class IncidentActionController {

    private final IncidentActionService incidentActionService;

    public IncidentActionController(IncidentActionService incidentActionService) {
        this.incidentActionService = incidentActionService;
    }

    @PostMapping
    public ResponseEntity<IncidentAction> addAction(@RequestBody IncidentAction action) {
        return ResponseEntity.status(HttpStatus.CREATED).body(incidentActionService.addAction(action));
    }

    @GetMapping
    public ResponseEntity<List<IncidentAction>> getAllActions() {
        return ResponseEntity.ok(incidentActionService.getAllActions());
    }
}
