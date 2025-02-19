package com.Megaminds.Recrutement.service;

import com.Megaminds.Recrutement.entity.Application;
import com.Megaminds.Recrutement.entity.Interview;
import com.Megaminds.Recrutement.repository.ApplicationRepository;
import com.Megaminds.Recrutement.repository.InterviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;

    public InterviewService(InterviewRepository interviewRepository, ApplicationRepository applicationRepository) {
        this.interviewRepository = interviewRepository;
        this.applicationRepository = applicationRepository;
    }

    public Interview scheduleInterview(Long applicationId, Interview interview) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        interview.setApplication(application);
        return interviewRepository.save(interview);
    }

    public List<Interview> getInterviewsByApplication(Long applicationId) {
        return interviewRepository.findByApplicationId(applicationId);
    }

    public Optional<Interview> getInterviewById(Long id) {
        return interviewRepository.findById(id);
    }

    public Interview updateInterview(Long id, Interview updatedInterview) {
        return interviewRepository.findById(id).map(interview -> {
            interview.setInterviewDate(updatedInterview.getInterviewDate());
            interview.setFeedback(updatedInterview.getFeedback());
            return interviewRepository.save(interview);
        }).orElseThrow(() -> new RuntimeException("Interview not found"));
    }

    public void deleteInterview(Long id) {
        interviewRepository.deleteById(id);
    }
}
