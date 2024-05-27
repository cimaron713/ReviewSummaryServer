package com.capstone.reviewsummary.Payment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
public class Order {

    @Id
    private int orderId;
    private int userId;
    private String kind;
    private int amount;
    // 이거 이넘 이넘 이넘 이넘 이넘
    private int status;
    private LocalDateTime localDateTime;
}
