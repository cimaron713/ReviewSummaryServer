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
        crawlingService.crawlCoupangReview("https://www.coupang.com/vp/product/reviews?productId=7694805260&page=2&size=5&sortBy=ORDER_SCORE_ASC&ratings=&q=&viRoleCode=3&ratingSummary=true");
        System.out.println("CwalTest.test");
    }
}
