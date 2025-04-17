package com.megaminds.assurance.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Lob
    private String image;

    private String email;

    @Enumerated(EnumType.STRING)
    private MaintenancePriority priority;

    @ManyToOne
    @JoinColumn(name = "id_contrat", referencedColumnName = "id", nullable = false)
    @JsonIgnoreProperties({"maintenances"})
    private Contrat contrat;

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status; // ✅ Utilisation d'un Enum au lieu de String

    @PrePersist
    @PreUpdate
    public void validateStatus() {
        if (this.status == null) {
            this.status = MaintenanceStatus.EN_ATTENTE; // Default status
        }
    }

    // Additional method to check expiration and set status
    public void checkAndSetStatus() {
        if (this.contrat != null && this.contrat.getDate_expiration() != null) {
            if (this.contrat.getDate_expiration().isBefore(LocalDate.now())) {
                this.status = MaintenanceStatus.REFUSE;
            } else {
                this.status = MaintenanceStatus.EN_ATTENTE;
            }
        } else {
            this.status = MaintenanceStatus.EN_ATTENTE; // Default status if no contrat or expiration date
        }
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Contrat getContrat() {
        return contrat;
    }

    public void setContrat(Contrat contrat) {
        this.contrat = contrat;
    }

    public MaintenanceStatus getStatus() {
        return status;
    }

    public void setStatus(MaintenanceStatus status) {
        this.status = status;
    }

    public MaintenancePriority getPriority() {
        return priority;
    }

    public void setPriority(MaintenancePriority priority) {
        this.priority = priority;
    }
}
