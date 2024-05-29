package com.capstone.reviewsummary.user.service.impl;

import com.capstone.reviewsummary.security.UserAdapter;
import com.capstone.reviewsummary.user.entity.User;
import com.capstone.reviewsummary.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = validUserBySocialId(email);
        return new UserAdapter(user);
    }

    private User validUserBySocialId(String socialId) throws UsernameNotFoundException {
        return userRepository.findBySocialId(socialId).get();
    }
}
