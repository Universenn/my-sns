package com.example.mysns.dto.user;

import com.example.mysns.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequest {
    private String email;
    private String nickname;
    private String password;

    public User toEntity(String encoded) {
        return User.builder()
                .email(this.email)
                .nickname(this.nickname)
                .password(encoded)
                .build();
    }

}
