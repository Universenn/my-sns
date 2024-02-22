package com.example.mysns.controller;

import com.example.mysns.dto.user.UserJoinRequest;
import com.example.mysns.dto.user.UserJoinResponse;
import com.example.mysns.dto.user.UserLoginRequest;
import com.example.mysns.dto.user.UserLoginResponse;
import com.example.mysns.exception.AppException;
import com.example.mysns.exception.ErrorCode;
import com.example.mysns.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRestController.class)
@WithMockUser
class UserRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper; // java object 를 json 형식으로 바꿔주는 잭슨의 오브젝트 이다

    // given
    String email = "email@gmail.com";
    String nickname = "juwan";
    String password = "qwer1234";
    String token = "token";

    UserJoinRequest userJoinRequest = new UserJoinRequest(email, nickname, password);
    UserLoginRequest userLoginRequest = new UserLoginRequest(nickname, password);

    @Test
    @DisplayName("회원가입 성공")
    void join_success() throws Exception {
        UserJoinResponse userJoinResponse = new UserJoinResponse(0L, userJoinRequest.getEmail());

        given(userService.join(any(UserJoinRequest.class))).willReturn(userJoinResponse);

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andExpect(status().isOk()) // 200
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.id").value(0L))
                .andExpect(jsonPath("$.result.email").value(email));

        verify(userService).join(any(UserJoinRequest.class));
    }

    @Test
    @DisplayName("회원가입 실패(이메일 중복)")
    void join_fail_1() throws Exception {
        given(userService.join(any(UserJoinRequest.class))).willThrow(new AppException(ErrorCode.DUPLICATED_EMAIL, ErrorCode.DUPLICATED_EMAIL.getMessage()));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andExpect(status().isConflict()) // 409
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.result.errorCode").value("DUPLICATED_EMAIL"))
                .andExpect(jsonPath("$.result.message").value("이미 사용 중인 이메일입니다."));

        verify(userService).join(any(UserJoinRequest.class));
    }

    @Test
    @DisplayName("회원가입 실패(닉네임 중복)")
    void join_fail_2() throws Exception {
        given(userService.join(any(UserJoinRequest.class))).willThrow(new AppException(ErrorCode.DUPLICATED_NICKNAME, ErrorCode.DUPLICATED_NICKNAME.getMessage()));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andExpect(status().isConflict()) // 409
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.result.errorCode").value("DUPLICATED_NICKNAME"))
                .andExpect(jsonPath("$.result.message").value("이미 사용 중인 닉네임입니다."));

        verify(userService).join(any(UserJoinRequest.class));
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() throws Exception {
        UserLoginResponse userLoginResponse = new UserLoginResponse(token);

        given(userService.login(any(UserLoginRequest.class))).willReturn(userLoginResponse);

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.jwt").value(token));

        verify(userService).login(any(UserLoginRequest.class));
    }


    @Test
    @DisplayName("로그인 실패(비밀번호 틀림)")
    void login_fail_2() throws Exception {
        given(userService.login(any(UserLoginRequest.class))).willThrow(new AppException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage()));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userLoginRequest)))
                .andExpect(status().isUnauthorized()) // 401
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.result.errorCode").value("INVALID_PASSWORD"))
                .andExpect(jsonPath("$.result.message").value("패스워드가 잘못되었습니다."));

        verify(userService).login(any(UserLoginRequest.class));
    }


}