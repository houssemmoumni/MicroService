package com.megaminds.finance.Service;

import com.megaminds.finance.Entity.FinancialReport;
import com.megaminds.finance.Repository.FinancialReportRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialReportService {
    @Autowired
    private FinancialReportRepository financialReportRepository;

    private final EmailService emailService;

    public FinancialReportService(EmailService emailService) {
        this.emailService = emailService;
    }


    public List<FinancialReport> getAllFinancialReports() {
        return financialReportRepository.findAll();
    }

    public FinancialReport getFinancialReportById(Long id) {
        return financialReportRepository.findById(id).orElseThrow(() -> new RuntimeException("Financial Report not found"));
    }

    public FinancialReport createFinancialReport(FinancialReport financialReport) {
        return financialReportRepository.save(financialReport);
    }
    public void generateAndSendReport(FinancialReport report) {
        try {
            // Assuming report is already populated with necessary data (e.g., from DB)
            emailService.sendFinancialReport(report);  // Send the financial report via email
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send financial report email", e);
        }
    }

    public FinancialReport updateFinancialReport(Long id, FinancialReport financialReportDetails) {
        FinancialReport financialReport = financialReportRepository.findById(id).orElseThrow(() -> new RuntimeException("Financial Report not found"));
        financialReport.setDate_rapport(financialReportDetails.getDate_rapport());
        financialReport.setTotal_revenue(financialReportDetails.getTotal_revenue());
        financialReport.setNet_profit(financialReportDetails.getNet_profit());
        return financialReportRepository.save(financialReport);
    }

    public void deleteFinancialReport(Long id) {
        FinancialReport financialReport = financialReportRepository.findById(id).orElseThrow(() -> new RuntimeException("Financial Report not found"));
        financialReportRepository.delete(financialReport);
    }
}