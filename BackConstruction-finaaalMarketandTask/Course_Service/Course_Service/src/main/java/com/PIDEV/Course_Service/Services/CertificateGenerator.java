package com.PIDEV.Course_Service.Services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CertificateGenerator {

    public static void generateCertificate(String filePath, String userName, String courseName, String qrCodeText) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Chemin local du logo
            String logoPath = "C:\\Users\\compuserv plus\\Downloads\\BackConstruction-main\\BackConstruction-main\\Course_Service\\Course_Service\\src\\main\\resources\\images\\img_1.png";

            // Charger le logo à partir du chemin local
            Image logo = Image.getInstance(logoPath); // Utilisez le chemin local
            logo.scaleToFit(100, 100); // Redimensionner le logo
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);

            // Ajouter un titre
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("Certificate of Completion", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Ajouter un sous-titre
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 18, BaseColor.BLACK);
            Paragraph subtitle = new Paragraph("This certifies that", subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(10);
            document.add(subtitle);

            // Ajouter le nom de l'utilisateur
            Font userNameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.BLUE);
            Paragraph userNameParagraph = new Paragraph(userName, userNameFont);
            userNameParagraph.setAlignment(Element.ALIGN_CENTER);
            userNameParagraph.setSpacingAfter(10);
            document.add(userNameParagraph);

            // Ajouter le nom du cours
            Font courseFont = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);
            Paragraph courseParagraph = new Paragraph("has successfully completed the course: " + courseName, courseFont);
            courseParagraph.setAlignment(Element.ALIGN_CENTER);
            courseParagraph.setSpacingAfter(20);
            document.add(courseParagraph);

            // Ajouter une table pour les informations supplémentaires
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(80);
            table.setSpacingBefore(20);
            table.setSpacingAfter(20);

            // Ajouter des cellules à la table
            addTableHeader(table);
            addTableRow(table, "Date of Issue", "2023-10-15");
            addTableRow(table, "Certificate ID", "123456");

            document.add(table);

            // Chemin local de la signature
            String signaturePath = "C:\\Users\\compuserv plus\\Downloads\\BackConstruction-main\\BackConstruction-main\\Course_Service\\Course_Service\\src\\main\\resources\\images\\img.png";

            // Charger la signature à partir du chemin local
            Image signature = Image.getInstance(signaturePath); // Utilisez le chemin local
            signature.scaleToFit(100, 50); // Redimensionner la signature
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

            // Générer et ajouter le QR code
            byte[] qrCode = generateQRCodeImage(qrCodeText, 200, 200);
            Image qrCodeImage = Image.getInstance(qrCode);
            qrCodeImage.scaleToFit(100, 100);
            qrCodeImage.setAlignment(Element.ALIGN_CENTER);
            document.add(qrCodeImage);

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void addTableHeader(PdfPTable table) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        PdfPCell headerCell = new PdfPCell(new Phrase("Field", headerFont));
        headerCell.setBackgroundColor(BaseColor.DARK_GRAY);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Value", headerFont));
        headerCell.setBackgroundColor(BaseColor.DARK_GRAY);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(headerCell);
    }

    private static void addTableRow(PdfPTable table, String field, String value) {
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        PdfPCell cell = new PdfPCell(new Phrase(field, cellFont));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(value, cellFont));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
    }

    private static byte[] generateQRCodeImage(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            return outputStream.toByteArray();
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }
}