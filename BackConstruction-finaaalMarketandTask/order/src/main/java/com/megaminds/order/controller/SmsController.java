package com.megaminds.order.controller;

import com.megaminds.order.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sms")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SmsController {
    private final SmsService smsService;

    @PostMapping("/send")
    public ResponseEntity<String> sendSms(
            @RequestParam String to,
            @RequestParam String message
    ) {
        smsService.sendSms(to, message);
        return ResponseEntity.ok("SMS envoyé avec succès !");
    }
}
