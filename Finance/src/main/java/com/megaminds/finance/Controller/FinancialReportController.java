package com.megaminds.finance.Controller;

import com.megaminds.finance.Entity.FinancialReport;
import com.megaminds.finance.Service.FinancialReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("http://localhost:4200")

@RestController
@RequestMapping("/api/financial-reports")
public class FinancialReportController {

    @Autowired
    private FinancialReportService financialReportService;

    @GetMapping
    public List<FinancialReport> getAllFinancialReports() {
        return financialReportService.getAllFinancialReports();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialReport> getFinancialReportById(@PathVariable Long id) {
        FinancialReport financialReport = financialReportService.getFinancialReportById(id);
        return ResponseEntity.ok(financialReport);
    }

    @PostMapping
    public FinancialReport createFinancialReport(@RequestBody FinancialReport financialReport) {
        return financialReportService.createFinancialReport(financialReport);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinancialReport> updateFinancialReport(@PathVariable Long id, @RequestBody FinancialReport financialReportDetails) {
        FinancialReport updatedFinancialReport = financialReportService.updateFinancialReport(id, financialReportDetails);
        return ResponseEntity.ok(updatedFinancialReport);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinancialReport(@PathVariable Long id) {
        financialReportService.deleteFinancialReport(id);
        return ResponseEntity.noContent().build();
    }
}