package com.sesac.paymentservice.service;

import com.sesac.paymentservice.entity.Payment;
import com.sesac.paymentservice.entity.PaymentStatus;
import com.sesac.paymentservice.event.product.in.PaymentRequestEvent;
import com.sesac.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PaymentService {
    private final PaymentRepository paymentRepository;


    @Transactional
    public Payment processPayment(PaymentRequestEvent paymentRequestEvent) {

        Payment payment = new Payment();

        payment.setOrderId(paymentRequestEvent.getOrderId());
        payment.setUserId(paymentRequestEvent.getUserId());
        payment.setAmount(paymentRequestEvent.getTotalAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentMethod("CARD");

        Payment savedPayment = paymentRepository.save(payment);


        try {
            Thread.sleep(2000);
            if (Math.random() < 2) {
                throw new RuntimeException("deposit not enough");
            }

            savedPayment.setStatus(PaymentStatus.COMPLETED);

            paymentRepository.save(savedPayment);

        } catch (Exception e) {
            savedPayment.setStatus(PaymentStatus.FAILED);
            savedPayment.setFailureReason(e.getMessage());
            paymentRepository.save(savedPayment);
        }


        return savedPayment;
    }

}
