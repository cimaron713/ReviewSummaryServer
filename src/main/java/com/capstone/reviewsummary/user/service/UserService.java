package com.capstone.reviewsummary.user.service;

import com.capstone.reviewsummary.user.dto.UserSignUpDto;

public interface UserService {
    void signUp(UserSignUpDto userSignUpDto) throws Exception;
}