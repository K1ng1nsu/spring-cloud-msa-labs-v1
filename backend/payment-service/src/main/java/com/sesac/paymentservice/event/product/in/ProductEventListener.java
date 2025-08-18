package com.sesac.paymentservice.event.product.in;

import com.sesac.paymentservice.entity.Payment;
import com.sesac.paymentservice.entity.PaymentStatus;
import com.sesac.paymentservice.event.order.out.OrderEventPublisher;
import com.sesac.paymentservice.event.product.out.ProductEventPublisher;
import com.sesac.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductEventListener {

    private final PaymentService paymentService;

    private final OrderEventPublisher orderEventPublisher;
    private final ProductEventPublisher productEventPublisher;

    @RabbitListener(queues = "${order.event.queue.payment-request}")
    public void handleOrderCreatedEvent(PaymentRequestEvent paymentRequestEvent) {
        log.info("Received Order Created Event with orderId: {}, amount: {}", paymentRequestEvent.getOrderId(), paymentRequestEvent.getTotalAmount());

        // try pay

        try {
            Payment payment = paymentService.processPayment(paymentRequestEvent);


            if (payment.getStatus() == PaymentStatus.COMPLETED) {
                // 1. success
                // Publish PaymentCompletedOrderCompletedEvent
                orderEventPublisher.publishPaymentCompletedOrderCompletedEvent(payment, paymentRequestEvent);

            } else if (payment.getStatus() == PaymentStatus.FAILED) {
                // 2. fail
                // Publish PaymentFailedOrderCanceledEvent
                // Publish PaymentFailedRestoreStockEvent

                orderEventPublisher.publishPaymentFailedOrderCancelEvent(paymentRequestEvent.getOrderId());
                productEventPublisher.publishPaymentFailedRestoreStockEvent(paymentRequestEvent);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("Handled PaymentRequestEvent");

    }


}

