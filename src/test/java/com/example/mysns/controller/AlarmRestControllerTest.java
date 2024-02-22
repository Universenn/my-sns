package com.example.mysns.controller;

import com.example.mysns.dto.alarm.AlarmResponse;
import com.example.mysns.entity.Alarm;
import com.example.mysns.entity.AlarmType;
import com.example.mysns.entity.User;
import com.example.mysns.exception.AppException;
import com.example.mysns.exception.ErrorCode;
import com.example.mysns.service.AlarmService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlarmRestController.class)
class AlarmRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AlarmService alarmService;

    @Autowired
    ObjectMapper objectMapper; // java object 를 json 형식으로 바꿔주는 잭슨의 오브젝트 이다


    @Test
    @WithMockUser
    @DisplayName("알람 조회 성공")
    void alarm_success() throws Exception {

        User targetUser = new User(1L, "email1@gmail.com", "targetUser", "password");
        User fromUser = new User(2L, "email2@gmail.com", "fromUser", "password");

        final Page<AlarmResponse> alarms = new PageImpl<>(List.of(
                AlarmResponse.of(new Alarm(1L, AlarmType.NEW_LIKE_ON_POST, targetUser, fromUser)),
                AlarmResponse.of(new Alarm(2L, AlarmType.NEW_COMMENT_ON_POST, targetUser, fromUser)),
                AlarmResponse.of(new Alarm(3L, AlarmType.NEW_LIKE_ON_POST, targetUser, fromUser))
        ));

        given(alarmService.findMyAlarms(any(), any(Pageable.class))).willReturn(alarms);

        mockMvc.perform(get("/api/v1/alarms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.content").isArray())
                .andExpect(jsonPath("$.result.pageable").exists())
                .andExpect(jsonPath("$.result.size").exists());


        verify(alarmService).findMyAlarms(any(), any(Pageable.class));
    }


    @Test
    @WithAnonymousUser
    @DisplayName("알람 조회 실패")
    void alarm_fail() throws Exception {
        given(alarmService.findMyAlarms(any(), any(Pageable.class))).willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(get("/api/v1/alarms"))
                .andExpect(status().is(ErrorCode.INVALID_PERMISSION.getHttpStatus().value()));
    }

}