package com.Megaminds.Recrutement.service;

import com.Megaminds.Recrutement.entity.Application;
import com.Megaminds.Recrutement.entity.Interview;
import com.Megaminds.Recrutement.repository.ApplicationRepository;
import com.Megaminds.Recrutement.repository.InterviewRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InterviewService {
    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final EmailService emailService;
    private final EmailScheduler emailScheduler;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public InterviewService(InterviewRepository interviewRepository,
                            ApplicationRepository applicationRepository,
                            EmailService emailService,
                            EmailScheduler emailScheduler) {
        this.interviewRepository = interviewRepository;
        this.applicationRepository = applicationRepository;
        this.emailService = emailService;
        this.emailScheduler = emailScheduler;
    }

    public Interview scheduleInterview(Long applicationId, Interview interview) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        interview.setApplication(application);
        return interviewRepository.save(interview);
    }

    public Interview scheduleAutomatedInterview(Application application) {
        Interview interview = new Interview();
        interview.setApplication(application);
        interview.setInterviewDate(LocalDate.now().plusDays(4));
        interview.setInterviewTime("14:00");
        interview.setFeedback("Please prepare for your technical interview");
        interview.setToken(UUID.randomUUID().toString());
        interview.setLinkActive(false);
        interview.setMeetLink("https://meet.google.com/" + UUID.randomUUID().toString().substring(0, 8));

        Interview savedInterview = interviewRepository.save(interview);
        scheduleInterviewEmails(application, savedInterview);
        return savedInterview;
    }

    private void scheduleInterviewEmails(Application application, Interview interview) {
        String candidateEmail = application.getCandidate().getEmail();
        String candidateName = application.getCandidate().getFirstName();
        String jobTitle = application.getJobOffer().getTitle();
        String interviewLink = frontendUrl + "/interview/" + interview.getToken();

        // Immediate confirmation
        emailService.sendEmail(
                candidateEmail,
                "Interview Scheduled for " + jobTitle,
                "Dear " + candidateName + ",\n\nYour interview for " + jobTitle +
                        " has been scheduled for " + interview.getInterviewDate() + " at " +
                        interview.getInterviewTime() + ".\n\nView details: " + interviewLink
        );

        // 24-hour reminder
        LocalDateTime reminderTime = LocalDateTime.of(
                interview.getInterviewDate().minusDays(1),
                LocalTime.of(9, 0)
        );
        emailScheduler.scheduleEmail(
                candidateEmail,
                "Reminder: Interview Tomorrow",
                "Your interview is tomorrow at " + interview.getInterviewTime(),
                reminderTime
        );

        // 1-hour reminder
        LocalDateTime finalReminder = LocalDateTime.of(
                interview.getInterviewDate(),
                LocalTime.parse(interview.getInterviewTime()).minusHours(1)
        );
        emailScheduler.scheduleEmail(
                candidateEmail,
                "Final Reminder: Interview in 1 Hour",
                "Your interview starts soon. Join here: " + interview.getMeetLink(),
                finalReminder
        );
    }

    public List<Interview> getInterviewsByApplication(Long applicationId) {
        return interviewRepository.findByApplicationId(applicationId);
    }

    public Optional<Interview> getInterviewById(Long id) {
        return interviewRepository.findById(id);
    }

    public Optional<Interview> findByToken(String token) {
        return interviewRepository.findByToken(token);
    }

    public Interview updateInterview(Long id, Interview updatedInterview) {
        return interviewRepository.findById(id).map(interview -> {
            interview.setInterviewDate(updatedInterview.getInterviewDate());
            interview.setInterviewTime(updatedInterview.getInterviewTime());
            interview.setFeedback(updatedInterview.getFeedback());
            interview.setMeetLink(updatedInterview.getMeetLink());
            return interviewRepository.save(interview);
        }).orElseThrow(() -> new RuntimeException("Interview not found"));
    }

    public void deleteInterview(Long id) {
        interviewRepository.deleteById(id);
    }

    public void activateInterviewLink(String token) {
        interviewRepository.findByToken(token).ifPresent(interview -> {
            if (interview.getInterviewDate().isBefore(LocalDate.now().plusDays(1))) {
                interview.setLinkActive(true);
                interviewRepository.save(interview);
            }
        });
    }
}