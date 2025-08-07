package com.sesac.orderservice.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private Long productId;

    private String productName;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal subtotal;
}
