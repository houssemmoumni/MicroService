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
public class FinancialReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_rapport;

    @Column(nullable = false)
    private LocalDate date_rapport;

    @Column(nullable = false)
    private Double total_revenue;

    @Column(nullable = false)
    private Double net_profit;
    private String email;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId_rapport() {
        return id_rapport;
    }

    public String getEmail() {
        return email;
    }
    @Lob // Use @Lob for large object data like Base64 encoded string
    private String signature;  // To store the signature as a Base64 string

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId_rapport(Long id_rapport) {
        this.id_rapport = id_rapport;
    }

    public LocalDate getDate_rapport() {
        return date_rapport;
    }

    public void setDate_rapport(LocalDate date_rapport) {
        this.date_rapport = date_rapport;
    }

    public Double getTotal_revenue() {
        return total_revenue;
    }

    public void setTotal_revenue(Double total_revenue) {
        this.total_revenue = total_revenue;
    }

    public Double getNet_profit() {
        return net_profit;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setNet_profit(Double net_profit) {
        this.net_profit = net_profit;
    }
}
