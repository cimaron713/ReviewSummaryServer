package com.capstone.reviewsummary.Converter;

import com.capstone.reviewsummary.dto.ResponseDTO;

public class SummaryConverter {
    public static ResponseDTO.SummaryResult SummaryDivide(String summary){
        String[] review = summary.split("\\n");
        return ResponseDTO.SummaryResult.builder()
                .pros(review[0])
                .cons(review[1])
                .comprehensive(review[2]).build();
    }
}
