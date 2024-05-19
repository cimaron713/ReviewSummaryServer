package com.capstone.reviewsummary.summary.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Getter
@RedisHash(timeToLive = 2592000)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Review {

    @Id
    private String id;
    private String productNo;
    private String brand;
    private String summary;
    private LocalDateTime createdAt;

    public Review(String productNo, String brand, String summary) {
        this.id = generateId(productNo, brand);
        this.productNo = productNo;
        this.brand = brand;
        this.summary = summary;
        this.createdAt = LocalDateTime.now();
    }
    public static String generateId(String productNo, String brand) {
        return brand + ":" + productNo;
    }
}
