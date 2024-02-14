package com.example.mysns.dto.user;

import com.example.mysns.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinResponse {
    private Long userId;
    private String email;

    public static UserJoinResponse of(User user) {
        return UserJoinResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .build();
    }
}
