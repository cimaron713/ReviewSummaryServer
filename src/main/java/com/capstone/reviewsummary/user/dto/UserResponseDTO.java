package com.capstone.reviewsummary.user.dto;

import com.capstone.reviewsummary.security.TokenDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserResponseDTO {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ResponseDTO {   // 로그인 회원가입 용 응답 dto
        private Long userId;
        private LocalDateTime createdAt;
        private TokenDTO tokenDTO;
    }
}
