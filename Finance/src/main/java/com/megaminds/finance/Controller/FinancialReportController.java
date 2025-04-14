package com.megaminds.finance.Controller;

import com.megaminds.finance.Entity.FinancialReport;
import com.megaminds.finance.Service.FinancialReportService;
import com.megaminds.finance.Service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
@CrossOrigin("http://localhost:4200")

@RestController
@RequestMapping("/api/financial-reports")
public class FinancialReportController {

    @Autowired
    private FinancialReportService financialReportService;
    @Autowired
    private PdfService pdfService;
    @GetMapping
    public List<FinancialReport> getAllFinancialReports() {
        return financialReportService.getAllFinancialReports();
    }

    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadReport(@RequestBody FinancialReport report) {
        // Save report with signature to the database (if required)
        financialReportService.saveFinancialReport(report); // Assuming there's a save method

        ByteArrayInputStream pdfStream = pdfService.generateReportPdf(report);
        byte[] pdfBytes = pdfStream.readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition
                .attachment()
                .filename("rapport_financier.pdf")
                .build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long id) {
        FinancialReport report = financialReportService.getFinancialReportById(id);
        ByteArrayInputStream pdfStream = pdfService.generateReportPdf(report);

        byte[] pdfBytes = pdfStream.readAllBytes();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=rapport_financier.pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
    @GetMapping("/{id}")
    public ResponseEntity<FinancialReport> getFinancialReportById(@PathVariable Long id) {
        FinancialReport financialReport = financialReportService.getFinancialReportById(id);
        return ResponseEntity.ok(financialReport);
    }
    @PostMapping("/send")
    public String sendReport(@RequestBody FinancialReport report) {
        financialReportService.generateAndSendReport(report);  // Send the financial report
        return "Financial Report sent successfully!";
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