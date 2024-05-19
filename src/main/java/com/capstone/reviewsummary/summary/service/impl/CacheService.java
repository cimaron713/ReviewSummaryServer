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

    private final ReviewRedisRepository reviewRedisRepository;
    private final CrawlingService crawlingService;
    private final ReviewSummaryServiceImpl reviewSummaryService;

    public String cachingCoupangReview(RequestDTO.CoupangRequestDTO coupangRequestDTO) throws IOException{
        String redisId = Review.generateId(coupangRequestDTO.getProductNo(),"coupang");
        Optional<Review> review = reviewRedisRepository.findById(redisId);

        if (review.isPresent()){
            return review.get().getSummary();
        }else{
            // 해당 상품의 리뷰 요약이 DB에 없는 경우
            String summary = reviewSummaryService.sendMessage(crawlingService.crawlCoupangReview(coupangRequestDTO));
            reviewRedisRepository.save(new Review(coupangRequestDTO.getProductNo(),"coupang",summary));
            return summary;
        }
    }

    public String cachingSmartStoreReview(RequestDTO.SmartStoreRequestDTO smartStoreRequestDTO) throws IOException {

        String redisId = Review.generateId(smartStoreRequestDTO.getOriginProductNo(),"smartstore");
        Optional<Review> review = reviewRedisRepository.findById(redisId);

        if (review.isPresent()){
            return review.get().getSummary();
        }else{
            // 해당 상품의 리뷰 요약이 DB에 없는 경우
            String summary = reviewSummaryService.sendMessage(crawlingService.crawlSmartStoreReview(smartStoreRequestDTO));
            reviewRedisRepository.save(new Review(smartStoreRequestDTO.getOriginProductNo(),"smartstore",summary));
            return summary;
        }
    }
}
