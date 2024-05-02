package com.capstone.reviewsummary.controller;

import com.capstone.reviewsummary.apiPayload.ApiResponse;
import com.capstone.reviewsummary.apiPayload.code.status.SuccessStatus;
import com.capstone.reviewsummary.common.urlDTO;
import com.capstone.reviewsummary.service.impl.CacheService;
import com.capstone.reviewsummary.service.impl.ReviewSummaryServiceImpl;
import com.capstone.reviewsummary.service.CrawlingService;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/summary")
@Slf4j
public class SummaryController {
    private final CacheService cacheService;

    @PostMapping("/smartstore")
    public ApiResponse<String> smartstoreSummary(@RequestBody urlDTO url) throws IOException {
        return ApiResponse.of(SuccessStatus.SUCCESS_SUMMARY, cacheService.cachingReview(url.getUrl()));
    }

//    @PostMapping("/hingguri")
//    public ApiResponse<String> crawl(@RequestBody urlDTO url) throws IOException {
//        String result  = crawlingService.crawlReview(url.getUrl());
//        if(result == null){
//            return ApiResponse.of(SuccessStatus.FAIL_CRAWLING, "힝구리요.");
//        }
//        else{
//            return ApiResponse.of(SuccessStatus.SUCCESS_CRAWLING, result);
//        }
//    }
}
