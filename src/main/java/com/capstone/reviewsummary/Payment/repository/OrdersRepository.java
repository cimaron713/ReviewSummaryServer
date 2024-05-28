package com.capstone.reviewsummary.Payment.repository;


import com.capstone.reviewsummary.Payment.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders,Integer> {
}
