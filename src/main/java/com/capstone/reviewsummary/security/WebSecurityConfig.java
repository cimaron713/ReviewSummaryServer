package com.capstone.reviewsummary.security;

import com.capstone.reviewsummary.user.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.DispatcherType;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity  // SpringSecurityFilterChain 자동 포함
@AllArgsConstructor     //  가능?
public class WebSecurityConfig {
    private UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http    // TODO: 배포 전 설정 -> cors, reqMatchers 등
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .headers((headerConfig) ->  //  X-Frame-Options 헤더를 설정, 클릭 재킹과 같은 공격을 방지
                        headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .authorizeHttpRequests(authorize -> authorize
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers( "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**",
                                "/social/**", "/user/login", "/user/join", "/api-docs", "/v3/**","/**"
                                ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .logout((logoutConfig) -> logoutConfig.logoutSuccessUrl("/"))
                // Jwt 을 통한 인증 방식을 사용하는 JwtAuthenticationFilter를 사용
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class)
                .userDetailsService(userDetailsService);

        return http.build();
    }
}