package com.capstone.reviewsummary.service;

import com.capstone.reviewsummary.dto.RequestDTO;

import java.io.IOException;
import java.util.List;

public interface CrawlingService {
    String crawlReview(String url) throws IOException;
    String crawlCoupangReview(RequestDTO.CoupangRequestDTO coupangRequestDTO) throws IOException;
    public String crawlSmartStoreReview(RequestDTO.SmartStoreRequestDTO smartStoreRequestDTO) throws IOException;
}
