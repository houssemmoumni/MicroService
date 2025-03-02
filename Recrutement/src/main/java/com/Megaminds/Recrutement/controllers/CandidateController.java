package com.Megaminds.Recrutement.controllers;

import com.Megaminds.Recrutement.entity.Candidate;
import com.Megaminds.Recrutement.service.CandidateService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Candidate> updateCandidate(
            @RequestParam("candidateId") Long candidateId,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("address") String address,
            @RequestParam("resume") MultipartFile resumeFile) {
        try {
            // Récupérez le candidat existant
            Optional<Candidate> candidateOpt = candidateService.getCandidateById(candidateId);
            if (candidateOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Mettez à jour les informations du candidat
            Candidate candidate = candidateOpt.get();
            candidate.setFirstName(firstName);
            candidate.setLastName(lastName);
            candidate.setEmail(email);
            candidate.setPhoneNumber(phoneNumber);
            candidate.setAddress(address);
            candidate.setResume(resumeFile.getBytes());

            // Enregistrez les modifications
            Candidate updatedCandidate = candidateService.createOrUpdateCandidate(candidate, resumeFile);
            return ResponseEntity.status(HttpStatus.OK).body(updatedCandidate);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        return ResponseEntity.ok(candidateService.getAllCandidates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable Long id) {
        return candidateService.getCandidateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/resume")
    public ResponseEntity<byte[]> getCandidateResume(@PathVariable Long id) {
        Optional<Candidate> candidate = candidateService.getCandidateById(id);
        if (candidate.isPresent() && candidate.get().getResume() != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resume.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(candidate.get().getResume());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }
}