package com.PIDEV.Communication_Service.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "Reponse")

public class Reponse implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Idreponse;

    private String titre;

    private String reponse;

    private LocalDate date_reponse;

    @Version
    private int version;




    @ManyToOne
    @JoinColumn(name = "reclamation_id") // Assurez-vous que cette colonne existe dans la table Reponse
    @JsonIgnore
    private Reclamation reclamation;

    @ManyToOne
    @JoinColumn(name = "user_id") // Ajoutez cette colonne pour lier la réponse à un utilisateur
    @JsonIgnore
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getIdreponse() {
        return Idreponse;
    }

    public void setIdreponse(Long idreponse) {
        Idreponse = idreponse;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public LocalDate getDate_reponse() {
        return date_reponse;
    }

    public void setDate_reponse(LocalDate date_reponse) {
        this.date_reponse = date_reponse;
    }

    public com.PIDEV.Communication_Service.Entities.Reclamation getReclamation() {
        return reclamation;
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;  // Utiliser 'this'
    }

}