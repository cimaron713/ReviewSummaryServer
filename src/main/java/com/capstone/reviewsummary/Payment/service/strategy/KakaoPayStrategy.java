package com.capstone.reviewsummary.Payment.service.strategy;


import com.capstone.reviewsummary.Payment.config.KakaoPayConfig;
import com.capstone.reviewsummary.Payment.domain.Orders;
import com.capstone.reviewsummary.Payment.dto.KakaoPayReadyResponseDTO;
import com.capstone.reviewsummary.Payment.repository.OrdersRepository;
import com.capstone.reviewsummary.user.entity.User;
import com.capstone.reviewsummary.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service("kakao")
@RequiredArgsConstructor
@Slf4j

public class KakaoPayStrategy implements PaymentStrategy{

    private final KakaoPayConfig kakaoPayConfig;
    private final OrdersRepository ordersRepository;
    private final UserRepository userRepository;
    @Override
    @Transactional
    public KakaoPayReadyResponseDTO readyPay(Long userId,String brand){

        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            Orders orders = createOrders(user.get(), brand, 1100);
            ordersRepository.save(orders);

            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(getBody(orders.getOrderId(), user.get().getId()), getHeader());
            RestTemplate restTemplate = new RestTemplate();
            //추후 예외처리 구현
            KakaoPayReadyResponseDTO kakaoPayReadyResponse = restTemplate.postForObject(kakaoPayConfig.getReadyURL(),
                    requestEntity,
                    KakaoPayReadyResponseDTO.class);

            orders.setCreatedAt(kakaoPayReadyResponse.getCreated_at());
            log.info(kakaoPayReadyResponse.toString());

            return kakaoPayReadyResponse;
        } else {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

    }

    private Orders createOrders(User user, String brand, int amount){
        Orders orders = Orders.builder()
                .kind(brand)
                .user(user)
                .amount(amount)
                .status(Orders.Status.INCOMPLETE)
                .build();

        return orders;
    }

    private HttpHeaders getHeader(){

        HttpHeaders httpHeaders = new HttpHeaders();
        //운영, 배포시에 Before : devsecretkey ->  After : secretkey 로 변경 필요함.
        httpHeaders.set("Authorization", "SECRET_KEY " + kakaoPayConfig.getDevsecretkey());
        httpHeaders.set("Content-Type", "application/json");

        return httpHeaders;
    }

    private Map<String,String> getBody(Long orderId,Long userId){

        Map<String, String> body = new HashMap<>();
        body.put("cid", "TC0ONETIME");
        body.put("partner_order_id", String.valueOf(orderId));
        body.put("partner_user_id", String.valueOf(userId));
        body.put("item_name", "리뷰 요약 1000회");
        body.put("quantity", "1");
        body.put("total_amount", "1100");
        body.put("tax_free_amount", "0");

        //페이지 추가 요망.
        body.put("approval_url", "http://localhost:8080/pa.html"); // 성공 시 redirect url
        body.put("cancel_url", "http://localhost:8080/pay.html"); // 취소 시 redirect url
        body.put("fail_url", "http://localhost:8080/pay.html"); // 실패 시 redirect url

        return body;
    }


}
