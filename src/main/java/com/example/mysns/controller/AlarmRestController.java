package com.example.mysns.controller;

import com.example.mysns.dto.alarm.AlarmResponse;
import com.example.mysns.entity.Response;
import com.example.mysns.service.AlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarms")
@Api(tags = {"Alarm API"})
public class AlarmRestController {

    private final AlarmService alarmService;

    @GetMapping("")
    @ApiOperation(value="알람", notes="특정 사용자의 글에 대한 알림 조회할 수 있습니다.")
    public Response<Page<AlarmResponse>> findMyAlarms(@ApiIgnore Principal principal, @PageableDefault(size = 20, sort = "createdDateTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AlarmResponse> alarmResponses = alarmService.findMyAlarms(principal.getName(), pageable);
        return Response.success(alarmResponses);
    }
}

