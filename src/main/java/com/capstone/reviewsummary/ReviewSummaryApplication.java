package com.capstone.reviewsummary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.capstone.reviewsummary.Payment.config")

public class ReviewSummaryApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReviewSummaryApplication.class, args);
    }
}