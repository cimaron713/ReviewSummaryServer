package com.capstone.reviewsummary;

import com.capstone.reviewsummary.service.CrawlingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CwalTest {
    @Autowired
    private CrawlingService crawlingService;

    @Test
    void test() throws Exception{
        crawlingService.crawlCoupangReview("https://www.coupang.com/vp/product/reviews?productId=7694805260&page=1&size=30&sortBy=DATE_DESC");
    }
    @Test
    void test2() throws Exception{
        crawlingService.cookieCheck("https://www.coupang.com/vp/product/reviews?productId=7694805260&page=1&size=30&sortBy=DATE_DESC");
        System.out.println("CwalTest.test");
    }
}
