package com.capstone.reviewsummary.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class GoogleSignUpDto {
    private String socialId;
    private String name;
    private String pictureUrl;
}