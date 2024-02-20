package com.example.mysns.dto.alarm;

import com.example.mysns.entity.Alarm;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AlarmResponse {

    private Long id;
    private String fromUser;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Builder
    public AlarmResponse(Long id, String fromUser, String message, LocalDateTime createdAt) {
        this.id = id;
        this.fromUser = fromUser;
        this.message = message;
        this.createdAt = createdAt;
    }

    public static AlarmResponse of(Alarm alarm) {
        return AlarmResponse.builder()
                .id(alarm.getId())
                .fromUser(alarm.getFromUser().getNickname())
                .message(alarm.getAlarmType().getMessage())
                .createdAt(alarm.getCreatedDateTime())
                .build();
    }
}
