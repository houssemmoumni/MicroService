package com.megaminds.finance.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Revenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_revenue;

    @Column(nullable = false)
    private String description_revenue;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDate date_revenue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryRevenue category;

    public Long getId_revenue() {
        return id_revenue;
    }

    public void setId_revenue(Long id_revenue) {
        this.id_revenue = id_revenue;
    }

    public String getDescription_revenue() {
        return description_revenue;
    }

    public void setDescription_revenue(String description_revenue) {
        this.description_revenue = description_revenue;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDate_revenue() {
        return date_revenue;
    }

    public void setDate_revenue(LocalDate date_revenue) {
        this.date_revenue = date_revenue;
    }

    public CategoryRevenue getCategory() {
        return category;
    }

    public void setCategory(CategoryRevenue category) {
        this.category = category;
    }
}
