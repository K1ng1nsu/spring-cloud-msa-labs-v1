package com.sesac.productservice.service;

import com.sesac.productservice.entity.Product;
import com.sesac.productservice.event.payment.in.PaymentFailedRestoreStockEvent;
import com.sesac.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product not found with id: " + id));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional
    public void decreaseStock(Long productId, Integer quantity) {
        Product byId = findById(productId);

        if (!isEnoughStock(byId, quantity)) {
            throw new RuntimeException("Not enough stock");
        }

        byId.setStockQuantity(byId.getStockQuantity() - quantity);

        productRepository.save(byId);

        log.info("*Decrease stock quantity successfully with productId: {} and remain quantity: {} ", productId, byId.getStockQuantity());
    }

    @Transactional
    public void restoreStock(PaymentFailedRestoreStockEvent paymentFailedRestoreStockEvent) {
        Long productId = paymentFailedRestoreStockEvent.getProductId();
        Integer quantity = paymentFailedRestoreStockEvent.getQuantity();

        Product byId = findById(productId);

        byId.setStockQuantity(byId.getStockQuantity() + quantity);

        productRepository.save(byId);
    }


    private boolean isEnoughStock(Product product, Integer quantity) {
        return product.getStockQuantity() >= quantity;
    }


}
