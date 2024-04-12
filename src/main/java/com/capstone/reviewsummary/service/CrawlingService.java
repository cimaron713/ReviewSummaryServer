package com.capstone.reviewsummary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlingService {

    private final SmartStoreCrawlingToolsImpl smartStoreCrawlingService;

    private List<String> crawlReview(CrawlingTools crawlingTools, String url){
        try {
            return crawlingTools.crawlReview(url);
        } catch (IOException e) {
            return null;
        }
    }

    public List<String> getReview(String url){
        return crawlReview(smartStoreCrawlingService,url);
    }

}
