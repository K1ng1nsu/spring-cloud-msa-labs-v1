package com.sesac.productservice.event.order.in;

import com.sesac.productservice.event.order.out.OrderEventPublisher;
import com.sesac.productservice.event.order.out.InventoryFailedEvent;
import com.sesac.productservice.event.payment.out.PaymentEventPublisher;
import com.sesac.productservice.event.payment.out.PaymentRequestEvent;
import com.sesac.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final ProductService productService;
    private final PaymentEventPublisher paymentEventPublisher;
    private final OrderEventPublisher orderEventPublisher;

    @RabbitListener(queues = "${order.event.queue.inventory}")
    public void handleOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent) {
        log.info("*Received OrderCreatedEvent - product-service with orderId {}", orderCreatedEvent.getOrderId());

        try {
            productService.decreaseStock(orderCreatedEvent.getProductId(), orderCreatedEvent.getQuantity());
            // when success
            // publish event - PaymentRequest
            PaymentRequestEvent paymentRequestEvent = createPaymentRequestEvent(orderCreatedEvent);

            paymentEventPublisher.publishPaymentRequestEvent(paymentRequestEvent);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("*Failed to decrease stock with productId : {} ", orderCreatedEvent.getProductId());

            // when fail
            // publish event - InventoryFail
            InventoryFailedEvent inventoryFailedEvent = createInventoryFailed(orderCreatedEvent, "Stock not enough");

            orderEventPublisher.publishInventoryFailedEvent(inventoryFailedEvent);


        }

    }

    private InventoryFailedEvent createInventoryFailed(OrderCreatedEvent orderCreatedEvent, String reason) {
        InventoryFailedEvent inventoryFailedEvent = new InventoryFailedEvent();

        inventoryFailedEvent.setOrderId(orderCreatedEvent.getOrderId());
        inventoryFailedEvent.setProductId(orderCreatedEvent.getProductId());
        inventoryFailedEvent.setQuantity(orderCreatedEvent.getQuantity());

        inventoryFailedEvent.setReason(reason);

        return inventoryFailedEvent;
    }

    private PaymentRequestEvent createPaymentRequestEvent(OrderCreatedEvent orderCreatedEvent) {
        PaymentRequestEvent paymentRequestEvent = new PaymentRequestEvent();

        paymentRequestEvent.setOrderId(orderCreatedEvent.getOrderId());
        paymentRequestEvent.setUserId(orderCreatedEvent.getUserId());
        paymentRequestEvent.setProductId(orderCreatedEvent.getProductId());
        paymentRequestEvent.setQuantity(orderCreatedEvent.getQuantity());
        paymentRequestEvent.setTotalAmount(orderCreatedEvent.getTotalAmount());

        return paymentRequestEvent;
    }
}
