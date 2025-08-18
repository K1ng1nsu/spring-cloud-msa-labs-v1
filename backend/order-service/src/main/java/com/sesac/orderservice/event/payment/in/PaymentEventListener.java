package com.sesac.orderservice.event.payment.in;

import com.sesac.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {
    private final OrderService orderService;

    // notification publisher 추가해야함(분리 부터 해야할 듯?)
//    private final

    // PaymentCompletedOrderCompletedEvent
    @RabbitListener(queues = "${order.event.queue.payment-completed-order-completed}")
    public void handlePaymentCompletedOrderCompletedEvent(PaymentCompletedOrderCompletedEvent paymentCompletedOrderCompletedEvent) {
        log.info("Received PaymentCompletedOrderCompletedEvent with orderId: {}, userId: {}, amount: {}",
                paymentCompletedOrderCompletedEvent.getOrderId()
                , paymentCompletedOrderCompletedEvent.getUserId()
                , paymentCompletedOrderCompletedEvent.getAmount()
        );

        orderService.handlePaymentCompleted(paymentCompletedOrderCompletedEvent);
    }

    @RabbitListener(queues = "${order.event.queue.payment-failed-order-cancel}")
    public void handlePaymentFailedOrderCanceledEvent(PaymentFailedOrderCanceledEvent paymentFailedOrderCanceledEvent) {
        log.info("Received PaymentCompletedOrderCompletedEvent with orderId: {}",
                paymentFailedOrderCanceledEvent.getOrderId()
        );

        orderService.handlePaymentFailed(paymentFailedOrderCanceledEvent);
    }


}
