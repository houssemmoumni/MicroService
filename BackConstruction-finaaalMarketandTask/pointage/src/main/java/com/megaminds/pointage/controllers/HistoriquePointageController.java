package com.megaminds.pointage.controllers;

import com.megaminds.pointage.entities.HistoriquePointage;
import com.megaminds.pointage.services.HistoriquePointageService;
import com.megaminds.pointage.services.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/pointage/historique-pointages")
public class HistoriquePointageController {

    @Autowired
    private HistoriquePointageService historiquePointageService;

    @Autowired
    private PdfService pdfService;

    @GetMapping("/{id}")
    public HistoriquePointage getHistoriquePointage(@PathVariable Long id) {
        return historiquePointageService.getHistoriquePointageById(id);
    }

    @GetMapping
    public List<HistoriquePointage> getAllHistoriquePointages() {
        return historiquePointageService.getAllHistoriquePointages();
    }

    @PostMapping
    public HistoriquePointage createHistoriquePointage(@RequestBody HistoriquePointage historiquePointage) {
        return historiquePointageService.createHistoriquePointage(historiquePointage);
    }

    @PutMapping("/{id}")
    public HistoriquePointage updateHistoriquePointage(@PathVariable Long id,
                                                       @RequestBody HistoriquePointage historiquePointage) {
        return historiquePointageService.updateHistoriquePointage(id, historiquePointage);
    }

    @DeleteMapping("/{id}")
    public void deleteHistoriquePointage(@PathVariable Long id) {
        historiquePointageService.deleteHistoriquePointage(id);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadHistoriquePointagePdf(@PathVariable Long id) {
        try {
            // Generate PDF
            ByteArrayInputStream pdfStream = pdfService.generateReportPdf(id);
            byte[] pdfBytes = pdfStream.readAllBytes();

            // Create filename with user ID and date
            HistoriquePointage pointage = historiquePointageService.getHistoriquePointageById(id);
            String filename = String.format("pointage_%s_%s.pdf",
                    pointage.getUser() != null ? pointage.getUser().getId() : "unknown",
                    LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));

            // Return PDF as downloadable file
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + filename + "\"")
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}