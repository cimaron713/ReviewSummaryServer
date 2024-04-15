package com.capstone.reviewsummary.service.impl;

import com.capstone.reviewsummary.common.ChatgptConfig;
import com.capstone.reviewsummary.service.ReviewSummaryService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ReviewSummaryServiceImpl implements ReviewSummaryService {
    private final String API_URL = "https://api.openai.com/v1/chat/completions";

    @Override
    public String sendMessage(String review){
        try {
            return sendRequest(review);
        } catch (IOException e) {
            return "An error occurred while sending the request: " + e.getMessage();
        }
    }

    @Override
    public String sendMessageAtom(String atom){
        try {
            return makeReviewSummary(atom);
        } catch (IOException e) {
            return "An error occurred while sending the request: " + e.getMessage();
        }
    }


    private String sendRequest(String prompt) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(API_URL);

        // Set headers
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Authorization", "Bearer " + ChatgptConfig.API_KEY);

        // Set request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "ft:gpt-3.5-turbo-0125:personal::92YANpQz");
        JSONArray messages = new JSONArray();

        // Create system message
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", """
                "상품명과 상품 리뷰들을 아래의 방식으로 처리하는 모델이다.\\n"
                                                  "1. 리뷰들은 '---' 을 기준으로 구분되며 1개씩 '원자'들로 추상화 한다. '특징 1개 (등장 횟수)' 구조를 원자라고 한다. 이때 상품명과 관련 없는 내용은 제외하며, 1개의 리뷰에는 다수의 원자가 존재할 수 있으나 형태가 다르지만 조금이라도 유사한 의미들을 공유하는 원자는 1개로 제한한다.(등장 횟수 1로 고정). 이것은 각 리뷰마다 동일한 가중치를 두기 위함이다.\\n"
                                                  "2. 각각의 리뷰들이 원자들로 추상화 되었으면 모든 원자들 중에 형태는 다르지만 조금이라도 유사한 의미들을 공유하는 원자들은 서로 통합을 하며, 이때 통합되는 원자들의 등장 횟수 또한 합하여 통합된 원자의 등장 횟수로 한다. 해당 원자가 몇 개의 리뷰에서 등장하였는지 고려하여 원자마다 다른 가중치를 두기 위함이다.\\n"
                                                  "3. 최종적으로 정리된 원자들은 줄바꿈을 구분자로하여 문자열로 출력한다."
                
                """);
        messages.put(systemMessage);


        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
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

        String atom = firstChoice.getJSONObject("message").get("content").toString();
        System.out.println(atom);

        return makeReviewSummary(atom);
       // return responseBody;
    }

    public String makeReviewSummary(String atom) throws IOException{

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
}

