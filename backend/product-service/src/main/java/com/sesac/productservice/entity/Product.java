package com.sesac.productservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "decimal(10,2)", nullable = false)
    private BigDecimal price;

    private Integer stockQuantity = 0;

    private String category;

    private String imageUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
