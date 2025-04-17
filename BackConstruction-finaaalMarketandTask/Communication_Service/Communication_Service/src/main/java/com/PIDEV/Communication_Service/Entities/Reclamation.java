package com.PIDEV.Communication_Service.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter

@Table(name = "Reclamation")

public class Reclamation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Idreclamation;

    private String description, titre, type;
    private LocalDateTime dateReclamation;
    private String status = "en attente";// Par exemple, 'en cours', 'résolu', 'fermé'

    @OneToMany(mappedBy = "reclamation" ,cascade =   CascadeType.ALL , fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Reponse> reponses = new ArrayList<>();


    @ManyToOne
    @JsonIgnore
    private User user;

    public Long getIdreclamation() {
        return Idreclamation;
    }

    public void setIdreclamation(Long idreclamation) {
        Idreclamation = idreclamation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDateReclamation() {
        return dateReclamation;
    }

    public void setDateReclamation(LocalDateTime dateReclamation) {
        this.dateReclamation = dateReclamation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Reponse> getReponses() {
        return reponses;
    }

    public void setReponses(List<Reponse> reponses) {
        this.reponses = reponses;
    }


    public com.PIDEV.Communication_Service.Entities.User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
