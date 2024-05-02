package com.capstone.reviewsummary.domain;

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
    private String productNo;
    private String summary;
    private LocalDateTime createdAt;

    public Review(String productNo,String summary){
        this.productNo = productNo;
        this.summary = summary;
        this.createdAt = LocalDateTime.now();
    }

}
