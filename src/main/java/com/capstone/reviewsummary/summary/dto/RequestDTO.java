package com.capstone.reviewsummary.summary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RequestDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SmartStoreRequestDTO{
        private String originProductNo;
        private String checkoutMerchantNo;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CoupangRequestDTO{
        private String productNo;
    }
}
