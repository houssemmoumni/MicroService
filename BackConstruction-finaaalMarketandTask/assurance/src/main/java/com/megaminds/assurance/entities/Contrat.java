package com.megaminds.assurance.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contratcondition;

    private LocalDate date_signature;

    private LocalDate date_expiration;

    // Relation ManyToOne avec Assurance
    @ManyToOne
    @JoinColumn(name = "id_assurance", referencedColumnName = "id", nullable = false) // L'assurance est obligatoire
    private Assurance assurance;

    @OneToMany(mappedBy = "contrat", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Maintenance> maintenances = new ArrayList<>();

    // Relation OneToOne avec Projet
    @OneToOne
    @JoinColumn(name = "id_projet", referencedColumnName = "id", nullable = false) // Le projet est obligatoire
    private Projet projet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContratcondition() {
        return contratcondition;
    }

    public void setContratcondition(String contratcondition) {
        this.contratcondition = contratcondition;
    }

    public LocalDate getDate_signature() {
        return date_signature;
    }

    public void setDate_signature(LocalDate date_signature) {
        this.date_signature = date_signature;
    }

    public LocalDate getDate_expiration() {
        return date_expiration;
    }

    public void setDate_expiration(LocalDate date_expiration) {
        this.date_expiration = date_expiration;
    }

    public Assurance getAssurance() {
        return assurance;
    }

    public void setAssurance(Assurance assurance) {
        this.assurance = assurance;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    public List<Maintenance> getMaintenances() {
        return maintenances;
    }

    public void setMaintenances(List<Maintenance> maintenances) {
        this.maintenances = maintenances;
    }
}
