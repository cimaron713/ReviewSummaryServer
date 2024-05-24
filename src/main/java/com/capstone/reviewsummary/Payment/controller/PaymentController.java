package com.capstone.reviewsummary.Payment.controller;

import com.capstone.reviewsummary.Payment.dto.PaymentRequestDTO;
import com.capstone.reviewsummary.Payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/payments")
    public void createPayment(@RequestBody PaymentRequestDTO paymentRequestDTO){
        paymentService.createPay(paymentRequestDTO.brand());
    }
}
