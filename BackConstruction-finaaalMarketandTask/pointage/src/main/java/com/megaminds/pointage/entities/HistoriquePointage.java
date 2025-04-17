package com.megaminds.pointage.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoriquePointage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("jour_pointage")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate jourPointage;

    @JsonProperty("temps_entree")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime tempsEntree;

    @JsonProperty("temps_sortie")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime tempsSortie;

    private String localisation;

    // Relation Many-to-One avec l'entité User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    @JsonIgnore
    private User user; // Relation avec User
    @JsonProperty("id_user")
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    private Integer score;

    @JsonProperty("temps_commencement")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime tempsCommencement;

    @JsonProperty("temps_finition")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime tempsFinition;
   /* @Transient // Prevent Hibernate from managing it as an entity
    private Ouvrier ouvrier;*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getJourPointage() {
        return jourPointage;
    }

    public void setJourPointage(LocalDate jourPointage) {
        this.jourPointage = jourPointage;
    }

    public LocalTime getTempsEntree() {
        return tempsEntree;
    }

    public void setTempsEntree(LocalTime tempsEntree) {
        this.tempsEntree = tempsEntree;
    }

    public LocalTime getTempsSortie() {
        return tempsSortie;
    }

    public void setTempsSortie(LocalTime tempsSortie) {
        this.tempsSortie = tempsSortie;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public LocalTime getTempsCommencement() {
        return tempsCommencement;
    }

    public void setTempsCommencement(LocalTime tempsCommencement) {
        this.tempsCommencement = tempsCommencement;
    }

    public LocalTime getTempsFinition() {
        return tempsFinition;
    }

    public void setTempsFinition(LocalTime tempsFinition) {
        this.tempsFinition = tempsFinition;
    }
}
