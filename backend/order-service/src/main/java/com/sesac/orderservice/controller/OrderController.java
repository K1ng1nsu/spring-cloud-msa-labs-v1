package com.sesac.orderservice.controller;

import com.sesac.orderservice.dto.OrderRequestDto;
import com.sesac.orderservice.entity.Order;
import com.sesac.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

//    @GetMapping
//    @Operation(summary = "Search Order list", description = "Search All Order")
//    public ResponseEntity<?> findAll() {
//        return ResponseEntity.ok(orderService.findAll());
//    }


    @GetMapping("/{id}")
    @Operation(summary = "Search Order", description = "Search Order with ID")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            Order byId = orderService.findById(id);
            return ResponseEntity.ok(byId);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> saveOrder(@RequestBody OrderRequestDto orderRequestDto) {
        try {
            Order order = orderService.createOrder(orderRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/my")
    @Operation(summary = "get My Order List", description = "Search list of order to login user")
    public ResponseEntity<?> getMyOrders(HttpServletRequest request) {
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        List<Order> orders = orderService.getOrdersByUserId(Long.parseLong(userIdHeader));

        return ResponseEntity.ok(orders);
    }

}
