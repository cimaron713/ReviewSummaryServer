package com.capstone.reviewsummary.user.service;

import com.capstone.reviewsummary.user.dto.GoogleSignUpDto;
import com.capstone.reviewsummary.user.dto.UserResponseDTO;

public interface UserService {
    boolean checkUser(String userId);
    void googleSignUp(GoogleSignUpDto googleSignUpDto);
    UserResponseDTO.ResponseDTO login(String userId);
}