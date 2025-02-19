package com.Megaminds.Recrutement.service;

import com.Megaminds.Recrutement.entity.Application;
import com.Megaminds.Recrutement.entity.ApplicationStatus;
import com.Megaminds.Recrutement.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    //Candidate applies for a job
    public Application applyForJob(Application application) {
        application.setStatus(ApplicationStatus.PENDING);
        return applicationRepository.save(application);
    }

    //Get all applications
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    //Get application by ID
    public Optional<Application> getApplicationById(Long id) {
        return applicationRepository.findById(id);
    }

    //Update application status
    public Application updateApplicationStatus(Long id, String status) {
        return applicationRepository.findById(id).map(application -> {
            application.setStatus(ApplicationStatus.valueOf(status.toUpperCase()));
            return applicationRepository.save(application);
        }).orElseThrow(() -> new RuntimeException("Application not found"));
    }

    //Delete an application
    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }
}
