package com.Megaminds.Recrutement.service;

import com.Megaminds.Recrutement.entity.Candidate;
import com.Megaminds.Recrutement.repository.CandidateRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public Candidate createOrUpdateCandidate(Candidate candidate, MultipartFile resumeFile) throws IOException {
        if (candidate.getId() != null) {
            // Si l'ID du candidat est présent, c'est une mise à jour
            Optional<Candidate> existingCandidateOpt = candidateRepository.findById(candidate.getId());
            if (existingCandidateOpt.isPresent()) {
                Candidate existingCandidate = existingCandidateOpt.get();
                existingCandidate.setFirstName(candidate.getFirstName());
                existingCandidate.setLastName(candidate.getLastName());
                existingCandidate.setEmail(candidate.getEmail());
                existingCandidate.setPhoneNumber(candidate.getPhoneNumber());
                existingCandidate.setAddress(candidate.getAddress());
                existingCandidate.setResume(resumeFile.getBytes());
                return candidateRepository.save(existingCandidate);
            }
        }
        // Si l'ID du candidat est absent, c'est une création
        candidate.setResume(resumeFile.getBytes());
        return candidateRepository.save(candidate);
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Optional<Candidate> getCandidateById(Long id) {
        return candidateRepository.findById(id);
    }

    public void deleteCandidate(Long id) {
        candidateRepository.deleteById(id);
    }
}