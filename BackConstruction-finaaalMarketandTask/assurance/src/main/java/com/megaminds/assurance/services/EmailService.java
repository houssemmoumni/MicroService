package com.megaminds.assurance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.from}") // Inject the "from" address from application.yml
    private String fromAddress;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    /**
     * Sends an email synchronously.
     *
     * @param to      Recipient email address
     * @param subject Email subject
     * @param text    Email body
     */
    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress); // Set the "from" address
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            logger.info("Email sent successfully to: {}", to);
        } catch (MailException e) {
            logger.error("Failed to send email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    /**
     * Sends a status change email asynchronously.
     *
     * @param title         Maintenance title
     * @param newStatus     New maintenance status
     * @param recipientEmail Recipient email address
     */

    public void sendStatusChangeEmail(String title, String newStatus, String recipientEmail) {
        String subject = "🔔 Changement de statut pour la maintenance : " + title;
        String message = "Le statut de la maintenance '" + title + "' a été mis à jour : " + newStatus;
        sendEmail(recipientEmail, subject, message);
    }
}