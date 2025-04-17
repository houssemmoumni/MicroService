package com.Megaminds.Recrutement.service;

import com.Megaminds.Recrutement.entity.*;
import com.Megaminds.Recrutement.repository.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContractService {
    private final ContractRepository contractRepository;
    private final CandidateRepository candidateRepository;
    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;

    private static final String COMPANY_NAME = "Megaminds";
    private static final String COMPANY_ADDRESS = "Ariana, Tunis, Ghazela";
    private static final String COMPANY_SIRET = "0123456789987456213";
    private static final String COMPANY_REPRESENTATIVE = "ESPRIT";
    private static final double GROSS_SALARY = 5000.0;
    private static final String COMPANY_LOGO_URL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTMZn-SXFk7Ehgs5EPl1XiFVcOEFXvzeE_k_A&s";

    public ContractService(
            ContractRepository contractRepository,
            CandidateRepository candidateRepository,
            InterviewRepository interviewRepository,
            ApplicationRepository applicationRepository
    ) {
        this.contractRepository = contractRepository;
        this.candidateRepository = candidateRepository;
        this.interviewRepository = interviewRepository;
        this.applicationRepository = applicationRepository;
    }

    public byte[] generateContractPdf(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));

        if (!isInterviewPassed(application)) {
            throw new IllegalStateException("Candidate hasn't passed the interview yet");
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();

            // Logo
            try {
                Image logo = Image.getInstance(new URL(COMPANY_LOGO_URL));
                logo.scaleToFit(80, 80);
                logo.setAlignment(Element.ALIGN_CENTER);
                document.add(logo);
                document.add(new Paragraph(" "));
            } catch (Exception e) {
                System.err.println("Could not load company logo: " + e.getMessage());
            }

            // Titre
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("CONTRAT DE TRAVAIL", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10f);
            document.add(title);

            // Référence contrat
            String contractNum = "CONTRACT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Font contractNumFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            Paragraph contractNumPara = new Paragraph("Référence: " + contractNum, contractNumFont);
            contractNumPara.setAlignment(Element.ALIGN_CENTER);
            contractNumPara.setSpacingAfter(20f);
            document.add(contractNumPara);

            // Sections
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

            // Entreprise
            Paragraph entrepriseHeader = new Paragraph("ENTREPRISE", sectionFont);
            entrepriseHeader.setSpacingAfter(5f);
            document.add(entrepriseHeader);

            List<String> entrepriseDetails = Arrays.asList(
                    COMPANY_NAME,
                    "Adresse: " + COMPANY_ADDRESS,
                    "SIRET: " + COMPANY_SIRET,
                    "Représenté par: " + COMPANY_REPRESENTATIVE
            );

            for (String detail : entrepriseDetails) {
                Paragraph p = new Paragraph(detail, normalFont);
                p.setSpacingAfter(3f);
                document.add(p);
            }

            document.add(new Paragraph(" "));

            // Employé
            Paragraph employeHeader = new Paragraph("EMPLOYÉ(E)", sectionFont);
            employeHeader.setSpacingAfter(5f);
            document.add(employeHeader);

            List<String> employeDetails = Arrays.asList(
                    "Nom: " + application.getCandidate().getLastName(),
                    "Prénom: " + application.getCandidate().getFirstName(),
                    "Poste: " + application.getJobOffer().getTitle(),
                    "Date de début: " + LocalDate.now().plusDays(14).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );

            for (String detail : employeDetails) {
                Paragraph p = new Paragraph(detail, normalFont);
                p.setSpacingAfter(3f);
                document.add(p);
            }

            document.add(new Paragraph(" "));

            // Termes
            Paragraph termesHeader = new Paragraph("CONDITIONS DU CONTRAT", sectionFont);
            termesHeader.setSpacingAfter(10f);
            document.add(termesHeader);

            List<String> termes = Arrays.asList(
                    "1. L'employé(e) occupera le poste de " + application.getJobOffer().getTitle(),
                    "2. Salaire brut mensuel: " + GROSS_SALARY + " TND",
                    "3. Horaires de travail: 35 heures par semaine",
                    "4. Congés payés: 25 jours par an",
                    "5. Période d'essai: 3 mois"
            );

            for (String terme : termes) {
                Paragraph p = new Paragraph(terme, normalFont);
                p.setSpacingAfter(5f);
                document.add(p);
            }

            // Signatures
            document.add(new Paragraph("\n"));
            PdfPTable signatureTable = new PdfPTable(2);
            signatureTable.setWidthPercentage(100);

            PdfPCell cell1 = new PdfPCell(new Paragraph("Fait à Tunis, le " +
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), normalFont));
            PdfPCell cell2 = new PdfPCell(new Paragraph("Fait à Tunis, le ___________________", normalFont));

            cell1.setBorder(Rectangle.NO_BORDER);
            cell2.setBorder(Rectangle.NO_BORDER);

            signatureTable.addCell(cell1);
            signatureTable.addCell(cell2);

            signatureTable.addCell(new Paragraph("Le Représentant " + COMPANY_NAME, normalFont));
            signatureTable.addCell(new Paragraph("L'Employé(e)", normalFont));

            document.add(signatureTable);

            // QR Code
            document.add(new Paragraph("\n"));
            Paragraph qrHeader = new Paragraph("CODE QR DE VÉRIFICATION", sectionFont);
            qrHeader.setAlignment(Element.ALIGN_CENTER);
            document.add(qrHeader);

            String qrContent = String.format(
                    "Contrat: %s\nEntreprise: %s\nEmployé: %s %s\nPoste: %s\nDate: %s",
                    contractNum,
                    COMPANY_NAME,
                    application.getCandidate().getLastName(),
                    application.getCandidate().getFirstName(),
                    application.getJobOffer().getTitle(),
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 100, 100);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            Image qrCodeImage = Image.getInstance(pngOutputStream.toByteArray());
            qrCodeImage.setAlignment(Element.ALIGN_CENTER);
            document.add(qrCodeImage);

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage(), e);
        }
    }

    private boolean isInterviewPassed(Application application) {
        return interviewRepository.findByApplicationId(application.getId())
                .stream()
                .anyMatch(i -> Boolean.TRUE.equals(i.getPassed()));
    }

    public Contract createContract(Long candidateId, Contract contract) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        contract.setCandidate(candidate);
        return contractRepository.save(contract);
    }

    public Optional<Contract> getContractByCandidate(Long candidateId) {
        return contractRepository.findByCandidateId(candidateId);
    }

    public Optional<Contract> getContractById(Long id) {
        return contractRepository.findById(id);
    }

    public Contract updateContract(Long id, Contract updatedContract) {
        return contractRepository.findById(id)
                .map(contract -> {
                    contract.setType(updatedContract.getType());
                    contract.setSignedDate(updatedContract.getSignedDate());
                    return contractRepository.save(contract);
                })
                .orElseThrow(() -> new RuntimeException("Contract not found"));
    }

    public void deleteContract(Long id) {
        contractRepository.deleteById(id);
    }
}