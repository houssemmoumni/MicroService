package com.megaminds.finance.Repository;

import com.megaminds.finance.Entity.FinancialReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialReportRepository extends JpaRepository<FinancialReport, Long> {
}
