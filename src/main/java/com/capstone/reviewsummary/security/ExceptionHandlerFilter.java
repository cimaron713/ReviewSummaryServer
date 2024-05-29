package com.capstone.reviewsummary.security;

import com.capstone.reviewsummary.apiPayload.code.status.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 다음 filter Chain에 대한 실행 (filter-chain의 마지막에는 Dispatcher Servlet이 실행된다.
            filterChain.doFilter(request, response);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("토큰의 유효기간 만료");
        } catch (JwtException e) {
            log.info("유효하지 않은 토큰");
        } catch (IllegalArgumentException | NoSuchElementException | UsernameNotFoundException e) {
            log.info("사용자를 찾을 수 없음");
        } catch (ArrayIndexOutOfBoundsException e) {
            log.info("토큰을 추출할 수 없음");
        } catch (NullPointerException e) {
            // 알 수 없는 에러
            log.info("Null Pointer Exception");
        }
    }
}
