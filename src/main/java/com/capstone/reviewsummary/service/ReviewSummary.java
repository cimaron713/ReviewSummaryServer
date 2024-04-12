package com.capstone.reviewsummary.service;

import com.capstone.reviewsummary.common.ChatgptConfig;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ReviewSummary {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static String makeReviewSummary(String atom) throws IOException{

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(API_URL);

        // Set headers
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Authorization", "Bearer " + ChatgptConfig.API_KEY);

        // Set request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo-0125");

        JSONArray messages = new JSONArray();
        // Create system message
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", """
                "1. '특징1개 (중복 횟수)' 구조를 '원자'라고 부르겠다. 원자들은 이전의 소비자들이 제품을 어떻게 평가했는지를 나타낸다.\\n"
                                "2. 이전의 소비자들이 제품을 어떻게 평가했는지를 알려주는걸 목적으로 장점, 단점, 종합들을 문장들로 구성해라. 만들때 중복되는 내용은 없어야하며, 중복 횟수를 우선순위로해서 있는 내용만 표현해라.\\n"
                                "3. 장점, 단점, 종합을 보는 대상은 구매할지 고민하는 소비자들이다.\\n"
                                "4. 출력 형식은 '장점: ~\\n단점: ~\\n종합: ~~~\\n'을 무조건 지켜야한다.\\n"
                """);
        messages.put(systemMessage);


        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", atom);
        messages.put(userMessage);


        requestBody.put("messages",messages);

        StringEntity params = new StringEntity(requestBody.toString(),"UTF-8");
        request.setEntity(params);

        // Execute HTTP request
        HttpResponse response = httpClient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        // Parse JSON response

        JSONObject jsonResponse = new JSONObject(responseBody);
        JSONArray choices = jsonResponse.getJSONArray("choices");
        JSONObject firstChoice = choices.getJSONObject(0);

        return firstChoice.getJSONObject("message").get("content").toString();

    }

    public static String sendMessage(String atom){
        try {
            String response = ReviewSummary.makeReviewSummary(atom);
            return response;
        } catch (IOException e) {
            return "An error occurred while sending the request: " + e.getMessage();
        }
    }


}
