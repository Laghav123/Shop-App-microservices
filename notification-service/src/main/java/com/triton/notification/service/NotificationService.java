package com.triton.notification.service;

import com.triton.order.event.OrderPlacedEvent;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender javaMailSender;

    @KafkaListener(topics = "order-placed")
    public void listenOrderPlaced(OrderPlacedEvent orderPlacedEvent) {
        log.info("Got message from order-placed topic {}", orderPlacedEvent);

        // Send email to customer emailAddress
        MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                messageHelper.setFrom("no-reply@triton.com");
                messageHelper.setTo(orderPlacedEvent.getEmail().toString());
                messageHelper.setSubject("Info regarding your order with OrderNumber " + orderPlacedEvent.getOrderNumber().toString());
                messageHelper.setText(String.format("""
                        Greetings,
                        
                        Your order with order number %s  has been placed successfully.
                        
                        Happy shopping!
                        Triton 
                        """, orderPlacedEvent.getOrderNumber().toString()));
            }
        };
        try {
            javaMailSender.send(messagePreparator);
            log.info("Email sent to {}", orderPlacedEvent.getEmail());
        }
        catch (Exception e){
            log.error("Failed to send email to {} ", orderPlacedEvent.getEmail());
            throw new RuntimeException(e);
        }
    }
}
