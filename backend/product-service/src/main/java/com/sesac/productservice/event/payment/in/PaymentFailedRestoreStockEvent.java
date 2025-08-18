package com.sesac.productservice.event.payment.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentFailedRestoreStockEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long orderId;
    private Long productId;
    private Integer quantity;
}
