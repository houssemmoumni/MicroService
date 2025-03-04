package com.megaminds.finance.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_budget;

    @Column(nullable = false)
    private Double total_amount;

    @Column(nullable = false)
    private Double sprint_amount;
    public Long getId_budget() {
        return id_budget;
    }

    // Setter pour id_budget
    public void setId_budget(Long id_budget) {
        this.id_budget = id_budget;
    }

    // Getter pour total_amount
    public Double getTotal_amount() {
        return total_amount;
    }

    // Setter pour total_amount
    public void setTotal_amount(Double total_amount) {
        this.total_amount = total_amount;
    }

    // Getter pour sprint_amount
    public Double getSprint_amount() {
        return sprint_amount;
    }

    // Setter pour sprint_amount
    public void setSprint_amount(Double sprint_amount) {
        this.sprint_amount = sprint_amount;
    }
}