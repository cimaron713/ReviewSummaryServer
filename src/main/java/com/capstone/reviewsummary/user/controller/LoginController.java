package com.capstone.reviewsummary.user.controller;

import com.capstone.reviewsummary.user.dto.UserResponseDTO;
import com.capstone.reviewsummary.user.service.impl.GoogleServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@Slf4j
public class LoginController {
    private final GoogleServiceImpl googleService;
    @GetMapping("/google")
    public UserResponseDTO.ResponseDTO GoogleLogin (@RequestParam("code") String code, HttpServletResponse response) {
        UserResponseDTO.ResponseDTO result = googleService.getGoogleAccessToken(code);
        Cookie cookie = new Cookie("access_token", result.getTokenDTO().getAccessToken());
        cookie.setMaxAge(60*60*24*7);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return result;
    }
}