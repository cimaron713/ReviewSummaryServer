package com.capstone.reviewsummary.Payment.service.strategy;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service("toss")
public class TossStrategy implements PaymentStrategy{
    @Override
    public String pay(){
        return "Test : TossStrategy";
    }
}
