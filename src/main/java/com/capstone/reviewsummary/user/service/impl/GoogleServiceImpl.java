package com.capstone.reviewsummary.user.service.impl;

import com.capstone.reviewsummary.user.dto.GoogleSignUpDto;
import com.capstone.reviewsummary.user.dto.UserResponseDTO;
import com.capstone.reviewsummary.user.service.GoogleService;
import com.capstone.reviewsummary.user.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class GoogleServiceImpl implements GoogleService {
    private final UserService userService;

    public UserResponseDTO.ResponseDTO getGoogleAccessToken(String accessCode) {
        String GOOGLE_TOKEN_URL = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";
        GOOGLE_TOKEN_URL += accessCode;
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(GOOGLE_TOKEN_URL,String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String jsonResponse = responseEntity.getBody();
            System.out.println(jsonResponse);
            try {
                // ObjectMapper를 사용하여 JSON 파싱
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);

                String googleUserId = jsonNode.get("id").asText();
                String name = jsonNode.get("name").asText();
                String pictureUrl = jsonNode.get("picture").asText();

                GoogleSignUpDto googleSignUpDto = GoogleSignUpDto.builder()
                        .socialId(googleUserId)
                        .name(name)
                        .pictureUrl(pictureUrl)
                        .build();

                if(!userService.checkUser(googleUserId)){
                    userService.googleSignUp(googleSignUpDto);
                }
                return userService.login(googleUserId);
            } catch (Exception e) {
                // 예외 처리
                e.printStackTrace();
            }
        }


        return null;
    }
}