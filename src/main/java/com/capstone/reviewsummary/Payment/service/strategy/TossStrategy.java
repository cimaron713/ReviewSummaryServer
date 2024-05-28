package com.capstone.reviewsummary.Payment.service.strategy;

import com.capstone.reviewsummary.Payment.dto.TossPayReadyResponseDTO;
import org.springframework.stereotype.Service;


@Service("toss")
public class TossStrategy implements PaymentStrategy{
    @Override
    public TossPayReadyResponseDTO readyPay(Long userId,String brand){
        return new TossPayReadyResponseDTO();
    }
}
