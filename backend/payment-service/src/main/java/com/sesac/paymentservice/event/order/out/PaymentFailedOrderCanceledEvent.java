package com.sesac.paymentservice.event.order.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentFailedOrderCanceledEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long orderId;

}
