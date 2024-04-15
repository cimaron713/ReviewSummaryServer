package com.capstone.reviewsummary.controller;

import com.capstone.reviewsummary.apiPayload.ApiResponse;
import com.capstone.reviewsummary.common.urlDTO;
import com.capstone.reviewsummary.service.impl.ReviewSummaryServiceImpl;
import com.capstone.reviewsummary.service.CrawlingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.capstone.reviewsummary.apiPayload.code.status.SuccessStatus.SUCCESS_SUMMARY;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/summary")
@Slf4j
public class SummaryController {
    private final CrawlingService crawlingService;
    private final ReviewSummaryServiceImpl reviewSummaryService;
    @PostMapping("/smartstore")
    public ApiResponse<String> smartstoreSummary(@RequestBody urlDTO url) throws IOException {
        return ApiResponse.of(SUCCESS_SUMMARY, reviewSummaryService.sendMessage(crawlingService.crawlReview(url.getUrl())));
    }

    @PostMapping("/hingguri")
    public String crawl(@RequestBody urlDTO url) throws IOException {
        return crawlingService.crawlReview(url.getUrl());
    }

}
