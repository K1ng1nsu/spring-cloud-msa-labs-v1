package com.sesac.paymentservice.event.product.in;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductEventListener {

    @RabbitListener(queues = "${order.event.queue.payment-request}")
    public void handleOrderCreatedEvent(PaymentRequestEvent paymentRequestEvent) {
        log.info("Received Order Created Event with orderId: {}, amount: {}", paymentRequestEvent.getOrderId(), paymentRequestEvent.getTotalAmount());

    }
}
