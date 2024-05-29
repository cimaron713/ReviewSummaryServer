package com.capstone.reviewsummary.user.service;

import com.capstone.reviewsummary.user.dto.UserResponseDTO;

public interface GoogleService {
    UserResponseDTO.ResponseDTO getGoogleAccessToken(String accessCode);
}
