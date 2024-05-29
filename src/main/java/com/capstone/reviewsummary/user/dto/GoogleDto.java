package com.capstone.reviewsummary.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GoogleDto {
    @NoArgsConstructor
    @Getter
    @Builder
    @AllArgsConstructor
    public static class GoogleSignUpDto {
        private String socialId;
        private String name;
        private String pictureUrl;
    }

    @NoArgsConstructor
    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserInfoDto {
        private String socialId;
        private String name;
        private String pictureUrl;
    }
}
