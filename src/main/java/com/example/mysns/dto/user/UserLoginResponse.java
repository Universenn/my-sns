package com.example.mysns.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginResponse {
    private String jwt;

    public UserLoginResponse(String jwt) {
        this.jwt = jwt;
    }
}
