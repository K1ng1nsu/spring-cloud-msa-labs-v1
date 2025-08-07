package com.sesac.productservice.controller;

import com.sesac.productservice.entity.Product;
import com.sesac.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Search Product list", description = "Search All Product")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }


    @GetMapping("/{id}")
    @Operation(summary = "Search Product", description = "Search Product with ID")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            Product byId = productService.findById(id);
            return ResponseEntity.ok(byId);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
