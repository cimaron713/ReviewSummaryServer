package com.capstone.reviewsummary.Payment.service.strategy;

import com.capstone.reviewsummary.Payment.dto.PayReadyResponseDTO;

public interface PaymentStrategy{
    PayReadyResponseDTO readyPay(Long userId,String brand);
}
