package com.megaminds.pointage.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class WhatsappService {

    private static final String ACCOUNT_SID = "ACf2d62cba402127c3a46368983a01d234";
    private static final String AUTH_TOKEN = "00484cd26b1b2d839916dad9ce4822ed";
    private static final String FROM_WHATSAPP_NUMBER = "whatsapp:+14155238886"; // Twilio sandbox number

    public WhatsappService() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void sendMessage(String toPhoneNumber, String messageBody) {
        try {
            Message message = Message.creator(
                    new PhoneNumber("whatsapp:" + toPhoneNumber),
                    new PhoneNumber(FROM_WHATSAPP_NUMBER),
                    messageBody
            ).create();

            System.out.println("✅ Message WhatsApp envoyé avec SID : " + message.getSid());

        } catch (Exception e) {
            System.err.println("❌ Échec de l'envoi WhatsApp : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
