package com.megaminds.assurance.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    // La relation avec le contrat est facultative et juste un ID de contrat
    @Column(nullable = true)
    private Long id_contrat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId_contrat() {
        return id_contrat;
    }

    public void setId_contrat(Long id_contrat) {
        this.id_contrat = id_contrat;
    }
}
