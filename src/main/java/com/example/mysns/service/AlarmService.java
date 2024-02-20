package com.example.mysns.service;

import com.example.mysns.dto.alarm.AlarmResponse;
import com.example.mysns.entity.User;
import com.example.mysns.exception.AppException;
import com.example.mysns.exception.ErrorCode;
import com.example.mysns.repository.AlarmRepository;
import com.example.mysns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    public Page<AlarmResponse> findMyAlarms(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new AppException(
                    ErrorCode.EMAIL_NOT_FOUND,
                    ErrorCode.EMAIL_NOT_FOUND.getMessage()
            );
        });
        return alarmRepository.findByTargetUser(user, pageable).map(AlarmResponse::of);
    }
}