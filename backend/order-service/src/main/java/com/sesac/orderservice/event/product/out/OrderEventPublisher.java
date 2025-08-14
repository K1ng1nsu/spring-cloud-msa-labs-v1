package com.sesac.orderservice.event.product.out;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${order.event.exchange}")
    private String exchange;

    @Value("${order.event.routing-key.notification}")
    private String notificationRoutingKey;

    @Value("${order.event.routing-key.inventory}")
    private String inventoryRoutingKey;


    public void publishOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent) {
        log.info("*Order Created Event for notification-service with OrderId : {}", orderCreatedEvent.getOrderId());
        rabbitTemplate.convertAndSend(exchange,notificationRoutingKey,orderCreatedEvent);

        log.info("*Order Created Event for product-service with OrderId : {}", orderCreatedEvent.getOrderId());
        rabbitTemplate.convertAndSend(exchange,inventoryRoutingKey,orderCreatedEvent);
    }

}
