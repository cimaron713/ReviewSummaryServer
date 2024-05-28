package com.capstone.reviewsummary.Payment.domain;

import com.capstone.reviewsummary.user.entity.User;
import io.netty.channel.local.LocalAddress;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
public class Orders{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String kind;

    private int amount;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime requestAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    public void setCreatedAt(LocalDateTime localDateTime){
        this.requestAt = localDateTime;
    }

    @Getter
    public enum Status {
        COMPLETE,INCOMPLETE
    }


}
