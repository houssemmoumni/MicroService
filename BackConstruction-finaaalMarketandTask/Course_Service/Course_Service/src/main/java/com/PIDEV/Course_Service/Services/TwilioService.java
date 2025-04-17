package com.PIDEV.Course_Service.Services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    public void sendSms(String toPhoneNumber, String message) {
        // Initialiser Twilio avec les identifiants du compte
        Twilio.init(accountSid, authToken);

        // Vérifier et corriger le format du numéro de téléphone
        if (!toPhoneNumber.startsWith("+")) {
            toPhoneNumber = "+216" + toPhoneNumber; // Add the country code for Tunisia
        }

        System.out.println("Numéro de téléphone de destination : " + toPhoneNumber); // Log pour vérification

        // Envoyer le SMS
        Message.creator(
                new PhoneNumber(toPhoneNumber), // Numéro de destination
                new PhoneNumber(twilioPhoneNumber), // Numéro Twilio
                message // Message à envoyer
        ).create();
    }


}