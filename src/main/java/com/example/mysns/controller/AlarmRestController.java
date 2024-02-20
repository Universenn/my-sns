package com.example.mysns.controller;

import com.example.mysns.dto.alarm.AlarmResponse;
import com.example.mysns.entity.Response;
import com.example.mysns.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarms")
public class AlarmRestController {

    private final AlarmService alarmService;

    @GetMapping("")
    public Response<Page<AlarmResponse>> findMyAlarms(Principal principal, @PageableDefault(size = 20, sort = "createdDateTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AlarmResponse> alarmResponses = alarmService.findMyAlarms(principal.getName(), pageable);
        return Response.success(alarmResponses);
    }
}

