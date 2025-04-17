package com.megaminds.finance.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.megaminds.finance.Entity.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
