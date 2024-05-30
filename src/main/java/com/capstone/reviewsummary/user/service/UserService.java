package com.capstone.reviewsummary.user.service;

import com.capstone.reviewsummary.user.dto.GoogleDto;
import com.capstone.reviewsummary.user.dto.GoogleSignUpDto;
import com.capstone.reviewsummary.user.dto.UserResponseDTO;

public interface UserService {
    void googleSignUp(GoogleDto.UserInfoDto userInfo);
    UserResponseDTO.ResponseDTO login(String accessCode);
}