package com.PIDEV.Course_Service.Controllers;

import com.PIDEV.Course_Service.Services.TwilioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class SmsController {

    @Autowired
    private TwilioService twilioService;

    @PostMapping("/send-sms")
    public ResponseEntity<String> sendSms(
            @RequestParam String toPhoneNumber, // Numéro de téléphone du destinataire
            @RequestParam String message // Message à envoyer
    ) {
        try {
            twilioService.sendSms(toPhoneNumber, message);
            return ResponseEntity.ok("SMS envoyé avec succès !");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi du SMS: " + e.getMessage());
            e.printStackTrace(); // Print the full stack trace for debugging
            return ResponseEntity.status(500).body("Erreur lors de l'envoi du SMS : " + e.getMessage());
        }
    }
}