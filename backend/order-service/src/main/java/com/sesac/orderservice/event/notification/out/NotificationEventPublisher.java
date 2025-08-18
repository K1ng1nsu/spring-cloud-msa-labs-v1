package com.sesac.orderservice.event.notification.out;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${order.event.exchange}")
    private String exchange;

    @Value("${order.event.routing-key.notification}")
    private String notificationRoutingKey;

}
