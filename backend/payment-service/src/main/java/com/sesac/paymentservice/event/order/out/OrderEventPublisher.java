package com.sesac.paymentservice.event.order.out;

import com.sesac.paymentservice.entity.Payment;
import com.sesac.paymentservice.event.product.in.PaymentRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${order.event.exchange}")
    private String exchange;


    @Value("${order.event.routing-key.payment-failed-order-cancel}")
    private String paymentFailedOrderCancelRoutingKey;

    @Value("${order.event.routing-key.payment-completed-order-completed}")
    private String paymentCompletedOrderCompletedRoutingKey;


    public void publishPaymentCompletedOrderCompletedEvent(Payment payment, PaymentRequestEvent paymentRequestEvent) {
        log.info("*Method_call - publishPaymentCompletedOrderCompletedEvent");

        PaymentCompletedOrderCompletedEvent paymentCompletedOrderCompletedEvent = createPaymentCompletedOrderCompletedEvent(paymentRequestEvent);
        rabbitTemplate.convertAndSend(exchange, paymentCompletedOrderCompletedRoutingKey, paymentCompletedOrderCompletedEvent);

        log.info("*Event_send - publishPaymentCompletedOrderCompletedEvent with paymentId: {}, userId: {},  orderId: {}, amount: {} ",
                payment.getId(),
                paymentCompletedOrderCompletedEvent.getUserId(),
                paymentCompletedOrderCompletedEvent.getOrderId(),
                paymentCompletedOrderCompletedEvent.getAmount());
    }

    public void publishPaymentFailedOrderCancelEvent(Long orderId) {
        log.info("*Method_call - publishPaymentFailedOrderCancelEvent");

        PaymentFailedOrderCanceledEvent paymentFailedOrderCanceledEvent = createPaymentFailedOrderCanceledEvent(orderId);
        rabbitTemplate.convertAndSend(exchange, paymentFailedOrderCancelRoutingKey, paymentFailedOrderCanceledEvent);

        log.info("*Event_send - publishPaymentFailedOrderCancelEvent with orderId: {}", orderId);
    }

    private PaymentCompletedOrderCompletedEvent createPaymentCompletedOrderCompletedEvent(PaymentRequestEvent paymentRequestEvent) {
        PaymentCompletedOrderCompletedEvent paymentCompletedOrderCompletedEvent = new PaymentCompletedOrderCompletedEvent();

        paymentCompletedOrderCompletedEvent.setUserId(paymentRequestEvent.getUserId());
        paymentCompletedOrderCompletedEvent.setOrderId(paymentRequestEvent.getOrderId());
        paymentCompletedOrderCompletedEvent.setAmount(paymentRequestEvent.getTotalAmount());

        return paymentCompletedOrderCompletedEvent;
    }

    private PaymentFailedOrderCanceledEvent createPaymentFailedOrderCanceledEvent(Long orderID) {
        PaymentFailedOrderCanceledEvent paymentFailedOrderCanceledEvent = new PaymentFailedOrderCanceledEvent();

        paymentFailedOrderCanceledEvent.setOrderId(orderID);

        return paymentFailedOrderCanceledEvent;
    }

}
