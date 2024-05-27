package com.capstone.reviewsummary.Payment.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class KakaoPayConfig {

    @Value("${spring.kakaopay.readyURL}")
    private String readyURL;

    @Value("${spring.kakaopay.secretkey}")
    private String secretkey;

    @Value("${spring.kakaopay.devsecretkey}")
    private String devsecretkey;
}
