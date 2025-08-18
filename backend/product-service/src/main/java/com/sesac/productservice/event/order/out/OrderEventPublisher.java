package com.sesac.productservice.event.order.out;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${order.event.exchange}")
    private String exchange;

    @Value("${order.event.routing-key.inventory-failed}")
    private String inventoryFailedRoutingKey;

    // 재고 차감 실패
    public void publishInventoryFailedEvent(InventoryFailedEvent inventoryFailedEvent) {
        log.info("*Failed to decrease stock with ProductId: {} OrderId: {}, reason: {}", inventoryFailedEvent.getProductId(), inventoryFailedEvent.getOrderId(), inventoryFailedEvent.getReason());
        rabbitTemplate.convertAndSend(exchange, inventoryFailedRoutingKey, inventoryFailedEvent);
        log.info("*Published Inventory Failed Event with reason: {} ", inventoryFailedEvent.getReason());
    }


}
