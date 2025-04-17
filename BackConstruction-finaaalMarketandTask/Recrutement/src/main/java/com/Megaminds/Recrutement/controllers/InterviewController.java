package com.Megaminds.Recrutement.controllers;

import com.Megaminds.Recrutement.dto.CompletedInterviewDTO;
import com.Megaminds.Recrutement.entity.ApplicationStatus;
import com.Megaminds.Recrutement.entity.Interview;
import com.Megaminds.Recrutement.service.InterviewService;
import com.Megaminds.Recrutement.repository.InterviewRepository;
import com.Megaminds.Recrutement.service.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/interviews")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class InterviewController {

    private final InterviewService interviewService;
    private final InterviewRepository interviewRepository;
    private final ApplicationService applicationService;

    public InterviewController(InterviewService interviewService,
                               InterviewRepository interviewRepository,
                               ApplicationService applicationService) {
        this.interviewService = interviewService;
        this.interviewRepository = interviewRepository;
        this.applicationService = applicationService;
    }

    @PostMapping("/{applicationId}")
    public ResponseEntity<Interview> scheduleInterview(@PathVariable Long applicationId, @RequestBody Interview interview) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(interviewService.scheduleInterview(applicationId, interview));
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

    @GetMapping("/by-token")
    public ResponseEntity<Interview> getInterviewByToken(@RequestParam String token) {
        return interviewService.findByToken(token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateLink(@RequestParam String token) {
        interviewService.activateInterviewLink(token);
        return ResponseEntity.ok("Interview link activated successfully");
    }

    @GetMapping("/completed")
    public ResponseEntity<List<CompletedInterviewDTO>> getCompletedInterviews() {
        List<Interview> interviews = interviewRepository.findByCompletedTrue();
        List<CompletedInterviewDTO> dtos = interviews.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<?> markAsCompleted(@PathVariable Long id) {
        try {
            Interview interview = interviewRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Interview not found with id: " + id));

            if (interview.getApplication() == null) {
                throw new RuntimeException("Interview with id " + id + " has no associated application");
            }

            if (interview.isCompleted()) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Interview was already completed",
                        "interviewId", interview.getId()
                ));
            }

            interview.setCompleted(true);
            interview.setPassed(true);
            interviewRepository.save(interview);

            applicationService.updateApplicationStatus(
                    interview.getApplication().getId(),
                    ApplicationStatus.INTERVIEW_PASSED
            );

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Interview marked as completed successfully",
                    "interviewId", interview.getId(),
                    "applicationStatus", ApplicationStatus.INTERVIEW_PASSED
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "Failed to complete interview",
                            "error", e.getMessage()
                    ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Interview> updateInterview(@PathVariable Long id, @RequestBody Interview updatedInterview) {
        return ResponseEntity.ok(interviewService.updateInterview(id, updatedInterview));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterview(@PathVariable Long id) {
        interviewService.deleteInterview(id);
        return ResponseEntity.noContent().build();
    }

    private CompletedInterviewDTO convertToDTO(Interview interview) {
        CompletedInterviewDTO dto = new CompletedInterviewDTO();
        dto.setId(interview.getId());
        dto.setApplicationId(interview.getApplication() != null ? interview.getApplication().getId() : null);
        dto.setInterviewDate(interview.getInterviewDate());

        if (interview.getApplication() != null && interview.getApplication().getCandidate() != null) {
            dto.setCandidateName(
                    interview.getApplication().getCandidate().getFirstName() + " " +
                            interview.getApplication().getCandidate().getLastName()
            );
        } else {
            dto.setCandidateName("Inconnu");
        }

        if (interview.getApplication() != null && interview.getApplication().getJobOffer() != null) {
            dto.setJobTitle(interview.getApplication().getJobOffer().getTitle());
        } else {
            dto.setJobTitle("Inconnu");
        }

        return dto;
    }
}