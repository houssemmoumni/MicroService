package com.Megaminds.Recrutement.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class EmailScheduler {

    private final EmailService emailService;
    private final ThreadPoolTaskScheduler taskScheduler;

    public EmailScheduler(EmailService emailService) {
        this.emailService = emailService;
        this.taskScheduler = new ThreadPoolTaskScheduler();
        this.taskScheduler.setPoolSize(5);
        this.taskScheduler.initialize();
    }

    public void scheduleEmail(String to, String subject, String text, LocalDateTime sendDateTime) {
        Runnable emailTask = () -> emailService.sendEmail(to, subject, text);
        Date sendTime = Date.from(sendDateTime.atZone(ZoneId.systemDefault()).toInstant());
        taskScheduler.schedule(emailTask, sendTime);
    }
}
