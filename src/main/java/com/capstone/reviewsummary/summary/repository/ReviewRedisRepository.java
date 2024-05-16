package com.capstone.reviewsummary.summary.repository;

import com.capstone.reviewsummary.summary.domain.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRedisRepository extends CrudRepository<Review, String> {
}
