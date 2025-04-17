package com.megaminds.material.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Setter
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private double availableQuantity;
    private BigDecimal price;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Enumerated(EnumType.STRING)
    private MaterialStatus status;
    private int createdBy;
    @OneToOne(mappedBy = "material",cascade = CascadeType.ALL)
    private Image image;

    public void updateStatus() {
        if (this.availableQuantity == 0) {
            this.status = MaterialStatus.NON_DISPONIBLE;
        }
    }

    public void setAvailableQuantity(double availableQuantity) {
        this.availableQuantity = availableQuantity;
        this.updateStatus(); // Update status whenever quantity changes
    }
}
