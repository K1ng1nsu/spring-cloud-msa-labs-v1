package com.sesac.productservice.event;

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

    @RabbitListener(queues = "${order.event.queue.inventory}")
    public void handleOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent) {
        log.info("*Received OrderCreatedEvent - product-service with orderId {}", orderCreatedEvent.getOrderId());

        try {
            productService.decreaseStock(orderCreatedEvent.getProductId(), orderCreatedEvent.getQuantity());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("*Failed to decrease stock with productId : {} ", orderCreatedEvent.getProductId());
        }

    }
}
