package com.sesac.productservice.event.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${order.event.exchange}")
    private String exchange;

    @Value("${order.event.routing-key.payment-request}")
    private String paymentRequestRoutingKey;

    // 재고 차감 성공시
    public void publishPaymentRequestEvent(PaymentRequestEvent paymentRequestEvent) {
        log.info("*Publishing payment request event with orderId: {}, totalAmount: {}", paymentRequestEvent.getOrderId(), paymentRequestEvent.getTotalAmount());
        rabbitTemplate.convertAndSend(exchange, paymentRequestRoutingKey, paymentRequestEvent);
        log.info("*Published payment request event with orderId: {}, totalAmount: {}", paymentRequestEvent.getOrderId(), paymentRequestEvent.getTotalAmount());
    }

}
