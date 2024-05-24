package com.capstone.reviewsummary.Payment.service.strategy;


import org.springframework.stereotype.Service;

@Service("kakao")
public class KakaoPayStrategy implements PaymentStrategy{
    @Override
    public String pay(){
        return "Test : KakaoPay";
    }
}
