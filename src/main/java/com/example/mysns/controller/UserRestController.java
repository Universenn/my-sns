package com.example.mysns.controller;

import com.example.mysns.dto.user.UserJoinRequest;
import com.example.mysns.dto.user.UserJoinResponse;
import com.example.mysns.dto.user.UserLoginRequest;
import com.example.mysns.dto.user.UserLoginResponse;
import com.example.mysns.entity.Response;
import com.example.mysns.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest dto){
        return Response.success(userService.join(dto));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> join(@RequestBody UserLoginRequest dto){
        return Response.success(userService.login(dto));
    }
}
