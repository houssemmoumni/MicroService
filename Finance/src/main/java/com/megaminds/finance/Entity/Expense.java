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
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_expense;

    @Column(nullable = false)
    private String description_expense;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDate expense_date;

    @ManyToOne
    @JoinColumn(name = "id_budget", nullable = false)
    private Budget budget;

    public Long getId_expense() {
        return id_expense;
    }

    public void setId_expense(Long id_expense) {
        this.id_expense = id_expense;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public LocalDate getExpense_date() {
        return expense_date;
    }

    public void setExpense_date(LocalDate expense_date) {
        this.expense_date = expense_date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription_expense() {
        return description_expense;
    }

    public void setDescription_expense(String description_expense) {
        this.description_expense = description_expense;
    }
}
