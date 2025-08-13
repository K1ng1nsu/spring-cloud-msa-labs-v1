package com.microservices.demo.notificationservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    @RabbitListener(queues = "${order.event.queue.notification}")
    public void handleOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent) {
        log.info("*Received OrderCreatedEvent - notification-service with orderId {}", orderCreatedEvent.getOrderId());

        try{
            Thread.sleep(3000);
            log.info("Sent email to the user - orderId {}", orderCreatedEvent.getOrderId());
        }catch (Exception e){
            log.error("Error while sending email to the user", e);
        }

    }
}
