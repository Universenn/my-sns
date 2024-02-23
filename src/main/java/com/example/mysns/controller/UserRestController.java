package com.example.mysns.controller;

import com.example.mysns.dto.user.UserJoinRequest;
import com.example.mysns.dto.user.UserJoinResponse;
import com.example.mysns.dto.user.UserLoginRequest;
import com.example.mysns.dto.user.UserLoginResponse;
import com.example.mysns.entity.Response;
import com.example.mysns.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"1_User API"})
@RequestMapping("/api/v1/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/join")
    @ApiOperation(value="회원가입", notes="email 과 password 를 이용해 회원가입을 할 수 있습니다.")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest dto){
        return Response.success(userService.join(dto));
    }

    @PostMapping("/login")
    @ApiOperation(value="로그인", notes="email 과 password 를 이용해 로그인을 할 수 있습니다.")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest dto){
        return Response.success(userService.login(dto));
    }
}
