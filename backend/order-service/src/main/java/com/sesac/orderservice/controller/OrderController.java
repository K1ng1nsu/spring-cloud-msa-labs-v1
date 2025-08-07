package com.sesac.orderservice.controller;

import com.sesac.orderservice.entity.Order;
import com.sesac.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
