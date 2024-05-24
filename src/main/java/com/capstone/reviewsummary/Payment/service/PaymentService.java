package com.capstone.reviewsummary.Payment.service;

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

    public void createPay(String brand){
        log.info(getStrategy(brand).pay());
    }
}
