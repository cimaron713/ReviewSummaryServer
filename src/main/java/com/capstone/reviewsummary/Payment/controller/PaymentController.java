package com.capstone.reviewsummary.Payment.controller;

import com.capstone.reviewsummary.Payment.dto.PayReadyResponseDTO;
import com.capstone.reviewsummary.Payment.dto.PaymentRequestDTO;
import com.capstone.reviewsummary.Payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/payments")
    public PayReadyResponseDTO readyPayment(@RequestBody PaymentRequestDTO paymentRequestDTO){
        long userId = 1;
        return paymentService.readyPay(paymentRequestDTO.getBrand(),userId);
    }
}
