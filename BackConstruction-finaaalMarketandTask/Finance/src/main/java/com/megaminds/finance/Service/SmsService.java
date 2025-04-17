package com.megaminds.finance.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private final TwilioConfig twilioConfig;

    @Autowired
    public SmsService(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    @PostConstruct
    public void init() {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
    }

    public void sendSms(String to, String content) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(to),
                    twilioConfig.getMessagingServiceSid(), // ✅ Use messaging service instead of 'From' number
                    content
            ).create();
            System.out.println("✅ SMS envoyé avec SID : " + message.getSid());
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'envoi du SMS : " + e.getMessage());
            throw new RuntimeException("Échec de l'envoi du SMS", e);
        }
    }
}
