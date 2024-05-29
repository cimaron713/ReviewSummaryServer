package com.capstone.reviewsummary.user.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "GOOGLES")
@AllArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String nickname; // 닉네임
    private String password; // 패스워드
    private String imageUrl; // 프로필 이미지
    private String socialId; // 소셜 아이디
    private String refreshToken; // 리프레시 토큰

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void destroyRefreshToken() {
        this.refreshToken = null;
    }
}
