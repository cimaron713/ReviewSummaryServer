package com.capstone.reviewsummary.service;

import java.io.IOException;
import java.util.List;

public interface CrawlingService {
    String crawlReview(String url) throws IOException;
    String crawlCoupangReview(String url) throws IOException;
}
