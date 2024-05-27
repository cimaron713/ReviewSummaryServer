package com.capstone.reviewsummary.Payment.service;

import com.capstone.reviewsummary.Payment.dto.PayReadyResponseDTO;
import com.capstone.reviewsummary.Payment.service.strategy.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final Map<String, PaymentStrategy> paymentStrategyMap;

    private PaymentStrategy getStrategy(String brand){
        return paymentStrategyMap.get(brand);
    }

    public PayReadyResponseDTO readyPay(String brand){
        return getStrategy(brand).readyPay();
    }
}
