package com.megaminds.finance.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.megaminds.finance.Entity.FinancialReport;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;  // This will be injected by Spring
    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Method to send MFA code
    public void sendMfaCode(String to, String code) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Your Two-Factor Authentication Code");

        String htmlContent = String.format("""
            <div style="font-family: Arial, sans-serif; padding: 20px; max-width: 600px; margin: auto;">
                <h2 style="color: #2c3e50;">Two-Factor Authentication Code</h2>
                <p>Your verification code is:</p>
                <div style="background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin: 20px 0; text-align: center;">
                    <h1 style="color: #2c3e50; margin: 0; font-size: 32px;">%s</h1>
                </div>
            </div>
        """, code);

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    // Method to send financial report email
    public void sendFinancialReport(FinancialReport report) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(report.getEmail());  // Send to the report's associated email
        helper.setSubject("Your Financial Report");

        String htmlContent = String.format("""
            <div style="font-family: Arial, sans-serif; padding: 20px; max-width: 600px; margin: auto;">
                <h2 style="color: #2c3e50;">Financial Report for %s</h2>
                <p>Date of Report: %s</p>
                <p>Total Revenue: $%.2f</p>
                <p>Net Profit: $%.2f</p>
                <p style="color: #e74c3c; font-weight: bold;">Thank you for your business!</p>
            </div>
        """, report.getDate_rapport(), report.getDate_rapport(), report.getTotal_revenue(), report.getNet_profit());

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
