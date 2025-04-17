package com.megaminds.notification.kafka;

import com.megaminds.notification.email.EmailService;
import com.megaminds.notification.entity.Notification;
import com.megaminds.notification.kafka.order.OrderConfirmation;
import com.megaminds.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;

import java.time.LocalDateTime;


@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationsConsumer {

    private final NotificationRepository repository;
    private final EmailService emailService;

    @KafkaListener(topics = "order-topic")
    public void consumeOrderConfirmationNotifications(OrderConfirmation orderConfirmation) throws MessagingException {
        log.info("Consuming the message from order-topic Topic", orderConfirmation);
        repository.save(
                Notification.builder()
                        .notificationDate(LocalDateTime.now())
                        .build()
        );
        System.out.println("ordery" + orderConfirmation.products());
        emailService.sendOrderConfirmationEmail(
                "selmi_yosri@outlook.fr",
                "yosri",
                orderConfirmation.totalAmount(),
                orderConfirmation.orderReference(),
                orderConfirmation.products()
        );
    }
}
