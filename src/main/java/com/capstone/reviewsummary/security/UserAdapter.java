package com.capstone.reviewsummary.security;

import com.capstone.reviewsummary.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserAdapter extends CustomUserDetails {
    @NotNull
    private User user;

    public UserAdapter(User user) {
        super(user);
        this.user = user;
    }
}