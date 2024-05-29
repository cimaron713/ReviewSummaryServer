package com.capstone.reviewsummary.user.controller;


import com.capstone.reviewsummary.user.service.UserService;
import com.capstone.reviewsummary.user.dto.UserSignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("/sign-up")
    public String signUp(@RequestBody UserSignUpDto userSignUpDto) throws Exception {
        //userService.signUp(userSignUpDto);
        return "회원가입 성공";
    }
}