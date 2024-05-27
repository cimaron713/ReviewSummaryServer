package com.capstone.reviewsummary.Payment.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class PayReadyResponseDTO {
    private String tid;
    private String next_redirect_pc_url;
    private LocalDateTime created_at;
}
