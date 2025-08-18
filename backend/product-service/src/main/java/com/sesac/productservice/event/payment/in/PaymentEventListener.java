package com.sesac.productservice.event.payment.in;

import com.sesac.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {
    private final ProductService productService;

    @RabbitListener(queues = "${order.event.queue.payment-failed-inventory-restore}")
    public void handleOrderCreatedEvent(PaymentFailedRestoreStockEvent paymentFailedRestoreStockEvent) {
        productService.restoreStock(paymentFailedRestoreStockEvent);
    }
}
