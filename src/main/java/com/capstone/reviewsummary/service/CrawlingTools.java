package com.capstone.reviewsummary.service;

import java.io.IOException;
import java.util.List;

public interface CrawlingTools {
    List<String> crawlReview(String url) throws IOException;

}
