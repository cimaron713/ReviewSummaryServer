package com.capstone.reviewsummary.repository;

import com.capstone.reviewsummary.domain.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface ReviewRedisRepository extends CrudRepository<Review, String> {
}
