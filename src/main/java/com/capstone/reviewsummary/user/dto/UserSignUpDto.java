package com.capstone.reviewsummary.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class UserSignUpDto {
    private String email;
    private String password;
    private String nickname;
}