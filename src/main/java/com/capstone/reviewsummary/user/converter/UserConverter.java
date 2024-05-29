package com.capstone.reviewsummary.user.converter;

import com.capstone.reviewsummary.security.TokenDTO;
import com.capstone.reviewsummary.user.dto.UserResponseDTO;
import com.capstone.reviewsummary.user.entity.User;

import java.time.LocalDateTime;

public class UserConverter {
    public static UserResponseDTO.ResponseDTO toResponseDTO(User user, TokenDTO tokenDTO) {
        return UserResponseDTO.ResponseDTO.builder()
                .userId(user.getId())
                .tokenDTO(tokenDTO)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
