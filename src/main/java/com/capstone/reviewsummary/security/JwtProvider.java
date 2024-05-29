package com.capstone.reviewsummary.security;

import com.capstone.reviewsummary.user.entity.User;
import com.capstone.reviewsummary.user.service.UserService;
import com.capstone.reviewsummary.user.service.impl.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JwtProvider 토큰 생성, 토큰 복호화 및 정보 추출, 토큰 유효성 검증의 기능이 구현된 클래스.
 * @author rimsong
 * application.properties에 jwt.secret: 값을 넣어 설정 추가해준 뒤 사용합니다.
 * jwt.secret는 토큰의 암호화, 복호화를 위한 secret key로서 이후 HS256알고리즘을 사용하기 위해, 256비트보다 커야합니다.
 * 알파벳이 한 단어당 8bit니, 32글자 이상이면 됩니다! 너무 짧으면 에러가 뜹니다.
 */
@Component
@Slf4j
public class JwtProvider {
    private final Key key;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtProvider(@Value("${jwt.secret}") String secretKey, UserDetailsServiceImpl userDetailsService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userDetailsService = userDetailsService;
    }

    public TokenDTO generateTokenByUser(User user) {
        // 권한 가져오기
        Claims claims = Jwts.claims().setSubject(user.getSocialId());
        claims.put("nickname", user.getNickname());

        long now = (new Date()).getTime();

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(user.getSocialId())
                .setClaims(claims)
//                .setIssuer()
                .setIssuedAt(new Date())
                .setExpiration(new Date(now + 1000 * 60 * 30))   //  30분
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(now + 1000 * 60 * 60 * 24 * 7))  // 7일
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenDTO.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public UserDetails getUserDetailsFromToken(String accessToken) {
        String socialId = String.valueOf(getClaims(accessToken).get("sub"));
        return userDetailsService.loadUserByUsername(socialId);
    }

    /*
     * 토큰의 Claim 디코딩
     */
    private Claims getClaims(String token) {
        log.info("getAllClaims token = {}", token);
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) throws RuntimeException {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            log.info("통과");
            return true;
    }

}
