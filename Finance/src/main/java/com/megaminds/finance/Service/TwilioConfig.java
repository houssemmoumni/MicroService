package com.megaminds.finance.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String senderPhoneNumber;

    @Value("${twilio.messaging.service.sid}")
    private String messagingServiceSid; // ✅ Add this line

    public String getAccountSid() {
        return accountSid;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getSenderPhoneNumber() {
        return senderPhoneNumber;
    }

    public String getMessagingServiceSid() {
        return messagingServiceSid;
    }
}
