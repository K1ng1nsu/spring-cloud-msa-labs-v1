package com.sesac.paymentservice.event.product.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestoreStockEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

}
