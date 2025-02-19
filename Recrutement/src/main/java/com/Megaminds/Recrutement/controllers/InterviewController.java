package com.Megaminds.Recrutement.controllers;

import com.Megaminds.Recrutement.entity.Interview;
import com.Megaminds.Recrutement.service.InterviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping("/{applicationId}")
    public ResponseEntity<Interview> scheduleInterview(
            @PathVariable Long applicationId,
            @RequestBody Interview interview) {
        Interview newInterview = interviewService.scheduleInterview(applicationId, interview);
        return ResponseEntity.status(HttpStatus.CREATED).body(newInterview);
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<Interview>> getInterviewsByApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(interviewService.getInterviewsByApplication(applicationId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Interview> getInterviewById(@PathVariable Long id) {
        return interviewService.getInterviewById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Interview> updateInterview(
            @PathVariable Long id,
            @RequestBody Interview updatedInterview) {
        return ResponseEntity.ok(interviewService.updateInterview(id, updatedInterview));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterview(@PathVariable Long id) {
        interviewService.deleteInterview(id);
        return ResponseEntity.noContent().build();
    }
}
