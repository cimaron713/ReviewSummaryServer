package com.capstone.reviewsummary.Payment.service;

import com.capstone.reviewsummary.Payment.domain.Orders;
import com.capstone.reviewsummary.Payment.dto.PayReadyResponseDTO;
import com.capstone.reviewsummary.Payment.service.strategy.PaymentStrategy;
import com.capstone.reviewsummary.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final Map<String, PaymentStrategy> paymentStrategyMap;

    private PaymentStrategy getStrategy(String brand){
        return paymentStrategyMap.get(brand);
    }

    public PayReadyResponseDTO readyPay(String brand,Long userId){
        return getStrategy(brand).readyPay(userId,brand);
    }

}
