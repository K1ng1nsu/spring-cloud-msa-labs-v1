package com.sesac.orderservice.service;

import com.sesac.orderservice.client.ProductServiceClient;
import com.sesac.orderservice.client.UserServiceClient;
import com.sesac.orderservice.client.dto.ProductDto;
import com.sesac.orderservice.client.dto.UserDto;
import com.sesac.orderservice.dto.OrderRequestDto;
import com.sesac.orderservice.entity.Order;
import com.sesac.orderservice.entity.OrderStatus;
import com.sesac.orderservice.event.payment.in.PaymentCompletedOrderCompletedEvent;
import com.sesac.orderservice.event.payment.in.PaymentFailedOrderCanceledEvent;
import com.sesac.orderservice.event.product.out.OrderCreatedEvent;
import com.sesac.orderservice.event.product.out.OrderEventPublisher;
import com.sesac.orderservice.facade.UserServiceFacade;
import com.sesac.orderservice.repository.OrderRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;
    private final UserServiceFacade userServiceFacade;

    private final OrderEventPublisher orderEventPublisher;

    private final Tracer tracer;

    @Transactional
    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Order not found with id: " + id));
    }

    @Transactional
    public Order createOrder(OrderRequestDto orderRequestDto) {

        Span span = tracer
                .nextSpan()
                .name("createOrder")
                .tag("order.userId", orderRequestDto.getUserId())
                .tag("order.productId", orderRequestDto.getProductId())
                .start();

        try (Tracer.SpanInScope spanInScope = tracer.withSpan(span)) {
            UserDto userById = userServiceFacade.getUserByIdWithFallback(orderRequestDto.getUserId());
            ProductDto productById = productServiceClient.getProductById(orderRequestDto.getProductId());

            checkNull(userById, "User");
            checkNull(productById, "Product");

//            checkProductQuantity(productById, orderRequestDto.getQuantity());

            BigDecimal totalPrice = calculatePrice(productById, orderRequestDto.getQuantity());

            Order order = new Order();
            order.setUserId(userById.getId());
            order.setTotalAmount(totalPrice);
            order.setStatus(OrderStatus.PENDING);

            orderRepository.save(order);


            OrderCreatedEvent orderCreatedEvent = createOrderCreatedEvent(order, orderRequestDto.getQuantity(), orderRequestDto.getProductId());
            orderEventPublisher.publishOrderCreatedEvent(orderCreatedEvent);


            return order;
        } catch (Exception e) {
            span.tag("error", e.getMessage());

            throw e;
        } finally {
            span.end();
        }
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public void handlePaymentCompleted(PaymentCompletedOrderCompletedEvent paymentCompletedOrderCompletedEvent) {
        Order byId = findById(paymentCompletedOrderCompletedEvent.getOrderId());
        byId.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(byId);
    }

    @Transactional
    public void handlePaymentFailed(PaymentFailedOrderCanceledEvent paymentFailedOrderCanceledEvent) {
        Order byId = findById(paymentFailedOrderCanceledEvent.getOrderId());
        byId.setStatus(OrderStatus.CANCELED);
        orderRepository.save(byId);
    }

    private OrderCreatedEvent createOrderCreatedEvent(Order order, Integer quantity, Long productId) {
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();

        orderCreatedEvent.setOrderId(order.getId());
        orderCreatedEvent.setUserId(order.getUserId());
        orderCreatedEvent.setProductId(productId);

        orderCreatedEvent.setQuantity(quantity);
        orderCreatedEvent.setTotalAmount(order.getTotalAmount());
        orderCreatedEvent.setCreatedAt(order.getCreatedAt());

        return orderCreatedEvent;
    }

    private void checkProductQuantity(ProductDto productDto, Integer quantity) {
        if (productDto.getStockQuantity() < quantity) throw new RuntimeException("Stock Quantity Not Enough");
    }

    private BigDecimal calculatePrice(ProductDto productDto, Integer quantity) {
        return productDto.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    private void checkNull(Object shouldNotNullDto, String DtoName) {
        if (shouldNotNullDto == null) throw new RuntimeException(DtoName + "Must not be null");
    }

}
