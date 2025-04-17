package com.Megaminds.Recrutement.dto;

public class ApplicationDTO {
    private Long id;
    private String candidateFullName;
    private String candidateEmail;
    private String jobOfferTitle;
    private String status;
    private String resume; // Base64-encoded resume

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCandidateFullName() {
        return candidateFullName;
    }

    public void setCandidateFullName(String candidateFullName) {
        this.candidateFullName = candidateFullName;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    public String getJobOfferTitle() {
        return jobOfferTitle;
    }

    public void setJobOfferTitle(String jobOfferTitle) {
        this.jobOfferTitle = jobOfferTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
}