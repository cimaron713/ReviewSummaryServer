package com.capstone.reviewsummary.summary.service.impl;

import com.capstone.reviewsummary.summary.domain.Review;
import com.capstone.reviewsummary.summary.dto.RequestDTO;
import com.capstone.reviewsummary.summary.repository.ReviewRedisRepository;
import com.capstone.reviewsummary.summary.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheService {
    //삭제요망
    private final String API_URL = "https://api.openai.com/v1/chat/completions";


    private final ReviewRedisRepository reviewRedisRepository;
    private final CrawlingService crawlingService;
    private final ReviewSummaryServiceImpl reviewSummaryService;

    private String parseProductNo(String url){
        String productNo = url.replaceAll(".*/products/(\\d+)", "$1");
        return productNo;
    }

    //url로부터 상품번호를 파싱후 해당 상품의 요약이 DB에 저장되어있는지 확인한다.
    public String cachingReview(String url) throws IOException {
        String productNo = parseProductNo(url);

        Optional<Review> review = reviewRedisRepository.findById(productNo);
        if (review.isPresent()){
            return review.get().getSummary();
        }else{
            // 해당 상품의 리뷰 요약이 DB에 없는 경우
            String summary = reviewSummaryService.sendMessage(crawlingService.crawlReview(url));
            reviewRedisRepository.save(new Review(productNo,summary));
            return summary;
        }
    }
    public String cachingSmartStoreReview(RequestDTO.SmartStoreRequestDTO smartStoreRequestDTO) throws IOException {
        String productNo = parseProductNo(smartStoreRequestDTO.getOriginProductNo());

        Optional<Review> review = reviewRedisRepository.findById(productNo);
        if (review.isPresent()){
            return review.get().getSummary();
        }else{
            // 해당 상품의 리뷰 요약이 DB에 없는 경우
            String summary = reviewSummaryService.sendMessage(crawlingService.crawlSmartStoreReview(smartStoreRequestDTO));
            reviewRedisRepository.save(new Review(productNo,summary));
            return summary;
        }
    }
}
