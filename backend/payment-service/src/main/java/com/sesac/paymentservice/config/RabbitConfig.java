package com.sesac.paymentservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

    @Value("${order.event.exchange}")
    private String exchange;

    @Value("${order.event.queue.notification}")
    private String notificationQueue;

    @Value("${order.event.queue.inventory}")
    private String inventoryQueue;

    @Value("${order.event.queue.payment-request}")
    private String paymentRequestQueue;

    @Value("${order.event.queue.inventory-failed}")
    private String inventoryFailedQueue;

    @Value("${order.event.queue.payment-failed-inventory-restore}")
    private String paymentFailedInventoryRestoreQueue;

    @Value("${order.event.queue.payment-failed-order-cancel}")
    private String paymentFailedOrderCancelQueue;

    @Value("${order.event.queue.payment-completed-order-completed}")
    private String paymentCompletedOrderCompletedQueue;

    @Value("${order.event.routing-key.notification}")
    private String notificationRoutingKey;

    @Value("${order.event.routing-key.inventory}")
    private String inventoryRoutingKey;

    @Value("${order.event.routing-key.payment-request}")
    private String paymentRequestRoutingKey;

    @Value("${order.event.routing-key.inventory-failed}")
    private String inventoryFailedRoutingKey;

    @Value("${order.event.routing-key.payment-failed-inventory-restore}")
    private String paymentFailedInventoryRestoreRoutingKey;

    @Value("${order.event.routing-key.payment-failed-order-cancel}")
    private String paymentFailedOrderCancelRoutingKey;

    @Value("${order.event.routing-key.payment-completed-order-completed}")
    private String paymentCompletedOrderCompletedRoutingKey;


    // Exchange 정의
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(exchange);
    }

    // Queue 정의
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(notificationQueue).build();
    }

    @Bean
    public Queue inventoryQueue() {
        return QueueBuilder.durable(inventoryQueue).build();
    }

    @Bean
    public Queue paymentRequestQueue() {
        return QueueBuilder.durable(paymentRequestQueue).build();
    }

    @Bean
    public Queue inventoryFailedQueue() {
        return QueueBuilder.durable(inventoryFailedQueue).build();
    }

    @Bean
    public Queue paymentFailedInventoryRestoreQueue() {
        return QueueBuilder.durable(paymentFailedInventoryRestoreQueue).build();
    }

    @Bean
    public Queue paymentFailedOrderCancelQueue() {
        return QueueBuilder.durable(paymentFailedOrderCancelQueue).build();
    }

    @Bean
    public Queue paymentCompletedOrderCompletedQueue() {
        return QueueBuilder.durable(paymentCompletedOrderCompletedQueue).build();
    }

    // Binding 정의
    @Bean
    public Binding notificationBinding() {
        return BindingBuilder.bind(notificationQueue())
                .to(orderExchange())
                .with(notificationRoutingKey);
    }

    @Bean
    public Binding inventoryBinding() {
        return BindingBuilder.bind(inventoryQueue())
                .to(orderExchange())
                .with(inventoryRoutingKey);
    }

    @Bean
    public Binding paymentRequestBinding() {
        return BindingBuilder.bind(paymentRequestQueue())
                .to(orderExchange())
                .with(paymentRequestRoutingKey);
    }

    @Bean
    public Binding inventoryFailedBinding() {
        return BindingBuilder.bind(inventoryFailedQueue())
                .to(orderExchange())
                .with(inventoryFailedRoutingKey);
    }

    @Bean
    public Binding paymentFailedInventoryRestoreQueueBinding() {
        return BindingBuilder.bind(paymentFailedInventoryRestoreQueue())
                .to(orderExchange())
                .with(paymentFailedInventoryRestoreRoutingKey);
    }

    @Bean
    public Binding paymentFailedOrderCancelQueueBinding() {
        return BindingBuilder.bind(paymentFailedOrderCancelQueue())
                .to(orderExchange())
                .with(paymentFailedOrderCancelRoutingKey);
    }

    @Bean
    public Binding paymentCompletedOrderCompletedQueueBinding() {
        return BindingBuilder.bind(paymentCompletedOrderCompletedQueue())
                .to(orderExchange())
                .with(paymentCompletedOrderCompletedRoutingKey);
    }


    // JSON 메시지 컨버터 추가
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}