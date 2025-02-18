package com.Megaminds.Recrutement.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class JobOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String Title;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    private String Description;
    private LocalDate postedDate;

    @Enumerated(EnumType.STRING)
    private JobOfferStatus status;

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private User recruiter;

    @OneToMany(mappedBy = "jobOffer")
    private List<Application> applications;

    // Getter and Setter for postedDate
    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    //Add Getter and Setter for status
    public JobOfferStatus getStatus() {
        return status;
    }

    public void setStatus(JobOfferStatus status) {
        this.status = status;
    }
}
