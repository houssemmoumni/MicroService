package com.megaminds.finance.Entity;

import java.time.LocalDate;

public class ProjectDTO {
    private Long projet_id;
    private String projet_name;
    private String projet_description;
    private LocalDate start_date;
    private LocalDate end_date;
    private String projectManager;
    private String statut_projet;
    private Double budget_estime;
    private Float risque_retard;
    private String imagePath;
    private Float latitude;
    private Float longitude;

    public Long getProjet_id() {
        return projet_id;
    }

    public void setProjet_id(Long projet_id) {
        this.projet_id = projet_id;
    }

    public String getProjet_name() {
        return projet_name;
    }

    public void setProjet_name(String projet_name) {
        this.projet_name = projet_name;
    }

    public String getProjet_description() {
        return projet_description;
    }

    public void setProjet_description(String projet_description) {
        this.projet_description = projet_description;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getStatut_projet() {
        return statut_projet;
    }

    public void setStatut_projet(String statut_projet) {
        this.statut_projet = statut_projet;
    }

    public Double getBudget_estime() {
        return budget_estime;
    }

    public void setBudget_estime(Double budget_estime) {
        this.budget_estime = budget_estime;
    }

    public Float getRisque_retard() {
        return risque_retard;
    }

    public void setRisque_retard(Float risque_retard) {
        this.risque_retard = risque_retard;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }
// Getters et Setters
}
