
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
@CrossOrigin(origins = "http://localhost:4200")
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
        System.out.println("🔎 Recherche de l'entretien avec ID : " + id);
        Optional<Interview> interview = interviewService.getInterviewById(id);

        if (interview.isPresent()) {
            System.out.println("✅ Entretien trouvé : " + interview.get());
            return ResponseEntity.ok(interview.get());
        } else {
            System.out.println("❌ Aucun entretien trouvé pour ID : " + id);
            return ResponseEntity.notFound().build();
        }
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
