package com.megaminds.pointage.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob // Stockage en BLOB dans la base de données
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    @Column(name = "telephone", nullable = false)
    private Long telephone;

    // Relation OneToMany avec HistoriquePointage
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<HistoriquePointage> historiquePointages;

    // Constructeurs
    public User() {}

    public User(byte[] image, Long telephone) {
        this.image = image;
        this.telephone = telephone;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Long getTelephone() {
        return telephone;
    }

    public void setTelephone(Long telephone) {
        this.telephone = telephone;
    }

    public List<HistoriquePointage> getHistoriquePointages() {
        return historiquePointages;
    }

    public void setHistoriquePointages(List<HistoriquePointage> historiquePointages) {
        this.historiquePointages = historiquePointages;
    }
}
