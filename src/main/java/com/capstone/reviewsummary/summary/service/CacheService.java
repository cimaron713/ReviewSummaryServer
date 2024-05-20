package com.capstone.reviewsummary.summary.service;

import com.capstone.reviewsummary.summary.dto.RequestDTO;

import java.io.IOException;

public interface CacheService {
    String cachingCoupangReview(RequestDTO.CoupangRequestDTO coupangRequestDTO) throws IOException;
    String cachingSmartStoreReview(RequestDTO.SmartStoreRequestDTO smartStoreRequestDTO) throws IOException;
}
