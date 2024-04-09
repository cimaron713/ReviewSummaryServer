package com.capstone.reviewsummary.controller;

import com.capstone.reviewsummary.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TempRestController {

    @GetMapping("/test")
    public ApiResponse<String> testAPI(){
        return ApiResponse.onSuccess("힝구리");
    }

    @GetMapping("/health")
    public String healthChck() {
        return "health check!";
    }

    @GetMapping("/test2")
    public String test2() {
        return "test2";
    }
}