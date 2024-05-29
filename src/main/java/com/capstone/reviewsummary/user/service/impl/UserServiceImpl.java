package com.capstone.reviewsummary.user.service.impl;

import com.capstone.reviewsummary.security.JwtProvider;
import com.capstone.reviewsummary.security.TokenDTO;
import com.capstone.reviewsummary.user.converter.UserConverter;
import com.capstone.reviewsummary.user.dto.GoogleSignUpDto;
import com.capstone.reviewsummary.user.dto.UserResponseDTO;
import com.capstone.reviewsummary.user.entity.User;
import com.capstone.reviewsummary.user.repository.UserRepository;
import com.capstone.reviewsummary.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    @Override
    public boolean checkUser(String userId){
        return userRepository.findBySocialId(userId).isPresent();
    }
    @Override
    public void googleSignUp(GoogleSignUpDto googleSignUpDto){
        User user = User.builder()
                .socialId(googleSignUpDto.getSocialId())
                .imageUrl(googleSignUpDto.getPictureUrl())
                .nickname(googleSignUpDto.getName()).build();
        userRepository.save(user);
    }

    @Override
    public UserResponseDTO.ResponseDTO login(String userId){
        User user = userRepository.findBySocialId(userId).get();
        TokenDTO tokenDTO = jwtProvider.generateTokenByUser(user);
        return UserConverter.toResponseDTO(user, tokenDTO);
    }
}