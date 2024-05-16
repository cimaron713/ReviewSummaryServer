package com.capstone.reviewsummary.summary.service;

import com.capstone.reviewsummary.summary.dto.RequestDTO;

import java.io.IOException;

public interface CrawlingService {
    String crawlReview(String url) throws IOException;
    String crawlCoupangReview(RequestDTO.CoupangRequestDTO coupangRequestDTO) throws IOException;
    public String crawlSmartStoreReview(RequestDTO.SmartStoreRequestDTO smartStoreRequestDTO) throws IOException;
}
