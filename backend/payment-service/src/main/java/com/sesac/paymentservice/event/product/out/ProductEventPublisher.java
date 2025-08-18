package com.sesac.paymentservice.event.product.out;

import com.sesac.paymentservice.event.product.in.PaymentRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${order.event.exchange}")
    private String exchange;

    @Value("${order.event.routing-key.payment-failed-inventory-restore}")
    private String paymentFailedInventoryRestoreRoutingKey;

    public void publishPaymentFailedRestoreStockEvent(PaymentRequestEvent paymentRequestEvent) {
        log.info("*Method_called - publishPaymentFailedRestoreStockEvent");
        PaymentFailedRestoreStockEvent paymentFailedRestoreStockEvent = createPaymentFailedRestoreStockEvent(paymentRequestEvent);
        rabbitTemplate.convertAndSend(exchange, paymentFailedInventoryRestoreRoutingKey, paymentFailedRestoreStockEvent);

        log.info("*Event_send - PaymentFailedRestoreStockEvent with orderId: {}, productId: {}, quantity: {}",
                paymentFailedRestoreStockEvent.getOrderId(),
                paymentFailedRestoreStockEvent.getProductId(),
                paymentFailedRestoreStockEvent.getQuantity()
        );
    }

    private PaymentFailedRestoreStockEvent createPaymentFailedRestoreStockEvent(PaymentRequestEvent paymentRequestEvent) {
        PaymentFailedRestoreStockEvent event = new PaymentFailedRestoreStockEvent();

        event.setOrderId(paymentRequestEvent.getOrderId());
        event.setProductId(paymentRequestEvent.getProductId());
        event.setQuantity(paymentRequestEvent.getQuantity());

        return event;
    }

}
