package com.capstone.reviewsummary;

import com.capstone.reviewsummary.domain.Review;
import com.capstone.reviewsummary.repository.ReviewRedisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class redisTest {

    @Autowired
    private ReviewRedisRepository reviewRedisRepository;

    @Test
    void test() {
        Review review = new Review("0","0");
        reviewRedisRepository.save(review);
        Optional<Review> r = reviewRedisRepository.findById(String.valueOf(0));

        if (r.isPresent()){
            System.out.println(r.get());
        }else{
            System.out.println("널 사랑해");
        }

}}
