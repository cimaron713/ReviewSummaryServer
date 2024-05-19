package com.capstone.reviewsummary.summary.controller;

import com.capstone.reviewsummary.summary.converter.SummaryConverter;
import com.capstone.reviewsummary.apiPayload.ApiResponse;
import com.capstone.reviewsummary.apiPayload.code.status.SuccessStatus;
import com.capstone.reviewsummary.common.urlDTO;
import com.capstone.reviewsummary.summary.dto.RequestDTO;
import com.capstone.reviewsummary.summary.dto.ResponseDTO;
import com.capstone.reviewsummary.summary.service.ReviewSummaryService;
import com.capstone.reviewsummary.summary.service.impl.CacheService;
import com.capstone.reviewsummary.summary.service.CrawlingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/summary")
@Slf4j
public class SummaryController {
    private final CacheService cacheService;
    private final CrawlingService crawlingService;
    private final ReviewSummaryService reviewSummaryService;
//   지워야할듯
//    @PostMapping("/smartstore2")
//    public ApiResponse<ResponseDTO.SummaryResult> smartstoreSummaryTemp(@RequestBody urlDTO url) throws IOException {
//        return ApiResponse.of(SuccessStatus.SUCCESS_SUMMARY, SummaryConverter.SummaryDivide(cacheService.cachingReview(url.getUrl())));
//    }

    @PostMapping("/smartstore")
    public ApiResponse<ResponseDTO.SummaryResult> smartstoreSummary(@RequestBody RequestDTO.SmartStoreRequestDTO SmartStoreRequestDTO) throws IOException {
        return ApiResponse.of(SuccessStatus.SUCCESS_SUMMARY, SummaryConverter.SummaryDivide(cacheService.cachingSmartStoreReview(SmartStoreRequestDTO)));
    }

    @PostMapping("/coupang")
    public ApiResponse<ResponseDTO.SummaryResult> coupangSummary(@RequestBody RequestDTO.CoupangRequestDTO coupangRequestDTO) throws IOException {
        return ApiResponse.of(SuccessStatus.SUCCESS_SUMMARY, SummaryConverter.SummaryDivide(cacheService.cachingCoupangReview(coupangRequestDTO)));

    }
}
