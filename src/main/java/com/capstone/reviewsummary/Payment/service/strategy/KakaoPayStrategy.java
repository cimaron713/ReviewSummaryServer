package com.capstone.reviewsummary.Payment.service.strategy;


import com.capstone.reviewsummary.Payment.config.KakaoPayConfig;
import com.capstone.reviewsummary.Payment.dto.KakaoPayReadyResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service("kakao")
@RequiredArgsConstructor
@Slf4j
public class KakaoPayStrategy implements PaymentStrategy{

    private final KakaoPayConfig kakaoPayConfig;
    @Override
    public KakaoPayReadyResponseDTO readyPay(){

        HttpEntity<Map<String,String>> requestEntity = new HttpEntity<>(getBody(),getHeader());
        RestTemplate restTemplate = new RestTemplate();
        KakaoPayReadyResponseDTO kakaoPayReadyResponse = restTemplate.postForObject(kakaoPayConfig.getReadyURL(),
                requestEntity,
                KakaoPayReadyResponseDTO.class);

        log.info(kakaoPayReadyResponse.toString());

        return kakaoPayReadyResponse;
    }


    private HttpHeaders getHeader(){

        HttpHeaders httpHeaders = new HttpHeaders();
        //운영, 배포시에 Before : devsecretkey ->  After : secretkey 로 변경 필요함.
        httpHeaders.set("Authorization", "SECRET_KEY " + kakaoPayConfig.getDevsecretkey());
        httpHeaders.set("Content-Type", "application/json");

        return httpHeaders;
    }

    private Map<String,String> getBody(){

        Map<String, String> body = new HashMap<>();
        body.put("cid", "TC0ONETIME");
        body.put("partner_order_id", "가맹점 주문 번호");
        body.put("partner_user_id", "가맹점 회원 ID");
        body.put("item_name", "리뷰 요약 1000회");
        body.put("quantity", "1");
        body.put("total_amount", "1100");
        body.put("tax_free_amount", "0");

        //페이지 추가 요망.
        body.put("approval_url", "http://localhost:8080/pay.html"); // 성공 시 redirect url
        body.put("cancel_url", "http://localhost:8080/pay.html"); // 취소 시 redirect url
        body.put("fail_url", "http://localhost:8080/pay.html"); // 실패 시 redirect url

        return body;
    }


}
