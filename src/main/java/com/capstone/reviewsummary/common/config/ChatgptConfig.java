package com.capstone.reviewsummary.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChatgptConfig {
    public static String API_KEY;
    @Value("${chatgpt.API_KEY}")
    public void setApiKey(String apiKey) {
        ChatgptConfig.API_KEY = apiKey;
    }


}
