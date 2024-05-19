package com.capstone.reviewsummary.summary.converter;

import com.capstone.reviewsummary.summary.dto.ResponseDTO;

public class SummaryConverter {
    public static ResponseDTO.SummaryResult SummaryDivide(String summary) {

        String modifiedSummary = summary.replaceAll("\n","");

        int prosIdx = modifiedSummary.indexOf("장점");
        int consIdx = modifiedSummary.indexOf("단점");
        int comprehensiveIdx = modifiedSummary.indexOf("종합");

        String pros = modifiedSummary.substring(prosIdx,consIdx);
        String cons = modifiedSummary.substring(consIdx,comprehensiveIdx);
        String comprehensive = modifiedSummary.substring(comprehensiveIdx);

        return ResponseDTO.SummaryResult.builder()
                .pros(pros)
                .cons(cons)
                .comprehensive(comprehensive).build();
    }
}
