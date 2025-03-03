package com.Megaminds.Recrutement.controllers;

import com.Megaminds.Recrutement.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestEmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send-email")
    public String sendTestEmail() {
        String to = "hamidamoumni@gmail.com"; // Remplacez par l'adresse e-mail du destinataire
        String subject = "Test d'envoi d'e-mail";
        String text = "Ceci est un test d'envoi d'e-mail depuis Spring Boot.";

        emailService.sendEmail(to, subject, text);
        return "E-mail envoyé avec succès !";
    }
}