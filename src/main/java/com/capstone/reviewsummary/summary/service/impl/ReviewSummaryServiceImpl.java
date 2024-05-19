package com.capstone.reviewsummary.summary.service.impl;

import com.capstone.reviewsummary.common.config.ChatgptConfig;
import com.capstone.reviewsummary.summary.service.ReviewSummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    private String sendRequest(String prompt) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(API_URL);

        // Set headers
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Authorization", "Bearer " + ChatgptConfig.API_KEY);

        // Set request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "ft:gpt-3.5-turbo-0125:personal::9QWNdMLe");
        JSONArray messages = new JSONArray();

        // Create system message
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", """
            "상품명과 상품 리뷰들을 아래의 방식으로 처리하는 모델이다.\\n1. 리뷰들을 '---'으로 구분되며 1개씩 '원자'들로 추상화 한다.
             '상품의 특징 1개 (등장 횟수)' 구조를 원자라고 하며 예시로는 '맛있다 (1)' 이 있다, 원자는 상품을 일부 대표할 수 있는 수준이어야한다. 
             이때 상품명과 관련 없는 내용은 제외하며, 1개의 리뷰에는 다수의 원자가 존재할 수 있으나 형태가 다르지만 조금이라도 유사한 의미들을 공유하는 원자는 1개로 제한한다.(등장 횟수 1로 고정).
              이것은 각 리뷰마다 동일한 가중치를 두기 위함이다.\\n
            2. 각각의 리뷰들이 원자들로 추상화 되었으면 모든 원자들 중에 형태는 다르지만 조금이라도 유사한 의미들을 공유하는 원자들은 서로 통합을 하며,
            이때 통합되는 원자들의 등장 횟수 또한 합하여 통합된 원자의 등장 횟수로 한다. 해당 원자가 몇 개의 리뷰에서 등장하였는지 고려하여 원자마다 다른 가중치를 두기 위함이다.
            3. 최종적으로 정리된 원자들은 '\\n'으로 구분자로하여 문자열로 출력한다."
           """
//               legacy
//                "상품명과 상품 리뷰들을 아래의 방식으로 처리하는 모델이다.\\n"
//              "1. 리뷰들은 '---' 을 기준으로 구분되며 1개씩 '원자'들로 추상화 한다. '특징 1개 (등장 횟수)' 구조를 원자라고 한다. 이때 상품명과 관련 없는 내용은 제외하며,
//              1개의 리뷰에는 다수의 원자가 존재할 수 있으나 형태가 다르지만 조금이라도 유사한 의미들을 공유하는 원자는 1개로 제한한다.(등장 횟수 1로 고정). 이것은 각 리뷰마다 동일한 가중치를 두기 위함이다.\\n"
//              "2. 각각의 리뷰들이 원자들로 추상화 되었으면 모든 원자들 중에 형태는 다르지만 조금이라도 유사한 의미들을 공유하는 원자들은 서로 통합을 하며, 이때 통합되는 원자들의 등장 횟수 또한 합하여 통합된 원자의 등장 횟수로 한다.
//              해당 원자가 몇 개의 리뷰에서 등장하였는지 고려하여 원자마다 다른 가중치를 두기 위함이다.\\n"
//              "3. 최종적으로 정리된 원자들은 줄바꿈을 구분자로하여 문자열로 출력한다."
                
                );
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
        log.info(String.valueOf(jsonResponse));
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
        // "1. '특징1개 (중복 횟수)' 구조를 '원자'라고 부르겠다. 원자들은 이전의 소비자들이 제품을 어떻게 평가했는지를 나타낸다."
        //                "2. 이전의 소비자들이 제품을 어떻게 평가했는지를 알려주는걸 목적으로 장점, 단점, 종합들을 문장들로 구성해라. 만들때 중복되는 내용은 없어야하며, 중복 횟수를 우선순위로해서 있는 내용만 표현해라."
        //                "3. 장점, 단점, 종합을 보는 대상은 구매할지 고민하는 소비자들이다."
        systemMessage.put("content", """
               
                1. 너는 고객이 상품을 판단할 수 있도록 리뷰를 평가하여야한다.
                2. '원자'는 리뷰를 나타내는 특징이다. '원자'로 불리는 것들을 입력으로 주겠다. 반드시 '원자'의 내용으로 리뷰를 판단하라. 
                3. 리뷰를 판단할 때 장점,단점, 그리고 종합적인 의견으로 리뷰를 판단하여야한다.
                4. 리뷰 판단의 예시는 다음과 같다
                
                *예시 시작*
                
                장점 : 이 제품은 다양한 색상과 용량 선택 가능성으로 사용자에게 큰 만족감을 줍니다. 예쁜 민트 컬러를 포함한 다양한 색상 옵션은 개개인의 취향을 반영할 수 있는 폭넓은 선택을 제공하며, 
                선물용으로도 적합한 크기로 인해 특별한 날에 선물하기 좋습니다. 또한, 보온 및 보냉 성능이 뛰어나 음료를 적절한 온도로 유지할 수 있어 실용적이며, 밀폐력이 우수해 내용물이 새지 않고 안전하게 보관할 수 있다는 점도 큰 장점입니다.
              
                단점 : 반면, 이 제품은 몇 가지 단점을 가지고 있습니다. 먼저, 뚜껑이 무거워 사용 시 불편함을 초래할 수 있습니다. 또한, 포장이 미흡해 배송 중 제품이 손상될 우려가 있으며, 세척 방법에 대한 안내가 부족하여 관리가 어렵습니다. 내구성이 떨어져 오래 사용하기 어려울 수 있다는 점도 단점으로 꼽을 수 있습니다. 마지막으로, 배송 박스의 크기가 너무 커서 불편할 수 있습니다.
                 이러한 단점들이 개선된다면 더욱 완벽한 제품이 될 것입니다.
                
                종합 :
                이 제품은 다양한 색상과 용량 선택 가능성, 우수한 보온/보냉 성능 등으로 인해 사용자에게 여러 가지 장점을 제공합니다.
                 예쁜 민트 컬러와 선물용으로 적합한 크기 등은 특히 매력적인 요소입니다. 그러나 뚜껑이 무겁고 포장이 미흡하며, 
                 내구성이 부족한 점 등은 개선이 필요합니다. 전반적으로 가격 대비 만족스럽지만, 일부 단점들을 보완하면 더 좋은 제품이 될 것입니다.
                *예시 끝*
                
                출력형식 이외에는 장점,단점,종합이라는 단어를 절대 사용해서는 안된다. 리뷰 요약의 내용의 어투는 자연스러운 문장을 사용하고 예시와 같이 '~입니다'를 사용하라. 와 같은 어투를 사용하라. '~임','~은' 허용하지않는다.
                
                5. 출력형식을 반드시 지켜라. 출력형식은 아래와 같다.
                장점 : ~~~
                단점 : ~~~
                종합 : ~~~
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

        log.warn("GPT OUTPUT");
        log.warn("=====================================================");
        log.warn(firstChoice.getJSONObject("message").get("content").toString());

        return firstChoice.getJSONObject("message").get("content").toString();
    }
}

