package com.capstone.reviewsummary.user.service;

import com.capstone.reviewsummary.user.dto.GoogleDto;
import com.capstone.reviewsummary.user.dto.UserResponseDTO;

public interface GoogleService {
    GoogleDto.UserInfoDto getUserInfoGoogle(String accessCode);
}
