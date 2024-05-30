package com.capstone.reviewsummary.user.service.impl;

import com.capstone.reviewsummary.security.JwtProvider;
import com.capstone.reviewsummary.security.TokenDTO;
import com.capstone.reviewsummary.user.converter.UserConverter;
import com.capstone.reviewsummary.user.dto.GoogleDto;
import com.capstone.reviewsummary.user.dto.GoogleSignUpDto;
import com.capstone.reviewsummary.user.dto.UserResponseDTO;
import com.capstone.reviewsummary.user.entity.User;
import com.capstone.reviewsummary.user.repository.UserRepository;
import com.capstone.reviewsummary.user.service.GoogleService;
import com.capstone.reviewsummary.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final GoogleService googleService;
    private final JwtProvider jwtProvider;

    @Override
    public void googleSignUp(GoogleDto.UserInfoDto userInfo){
        User user = User.builder()
                .socialId(userInfo.getSocialId())
                .imageUrl(userInfo.getPictureUrl())
                .nickname(userInfo.getName()).build();
        userRepository.save(user);
    }

    @Override
    public UserResponseDTO.ResponseDTO login(String accessCode){
        GoogleDto.UserInfoDto userInfo = googleService.getUserInfoGoogle(accessCode);
        if(!checkUser(userInfo.getSocialId())){
            googleSignUp(userInfo);
        }
        User user = userRepository.findBySocialId(userInfo.getSocialId()).get();
        TokenDTO tokenDTO = jwtProvider.generateTokenByUser(user);
        return UserConverter.toResponseDTO(user, tokenDTO);
    }

    public boolean checkUser(String userId){
        return userRepository.findBySocialId(userId).isPresent();
    }

}