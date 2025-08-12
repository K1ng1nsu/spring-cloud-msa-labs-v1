package com.sesac.orderservice.service;

import com.sesac.orderservice.client.ProductServiceClient;
import com.sesac.orderservice.client.UserServiceClient;
import com.sesac.orderservice.client.dto.ProductDto;
import com.sesac.orderservice.client.dto.UserDto;
import com.sesac.orderservice.dto.OrderRequestDto;
import com.sesac.orderservice.entity.Order;
import com.sesac.orderservice.facade.UserServiceFacade;
import com.sesac.orderservice.repository.OrderRepository;
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

    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Order not found with id: " + id));
    }

    @Transactional
    public Order createOrder(OrderRequestDto orderRequestDto) {
        UserDto userById = userServiceFacade.getUserByIdWithFallback(orderRequestDto.getUserId());
        ProductDto productById = productServiceClient.getProductById(orderRequestDto.getProductId());

        checkNull(userById, "User");
        checkNull(productById, "Product");

        checkProductQuantity(productById, orderRequestDto.getQuantity());

        BigDecimal totalPrice = calculatePrice(productById, orderRequestDto.getQuantity());

        Order order = new Order();
        order.setUserId(userById.getId());
        order.setTotalAmount(totalPrice);
        order.setStatus("COMPLETED");

        orderRepository.save(order);

        return order;
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
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
