package com.example.mysns.observer.events;

import com.example.mysns.entity.Alarm;
import com.example.mysns.entity.AlarmType;
import com.example.mysns.entity.User;

public class AlarmEvent {
    private final Alarm alarm;

    public AlarmEvent(Alarm alarm) {
        this.alarm = alarm;
    }

    public static AlarmEvent of(AlarmType alarmType, User target, User from) {
        return new AlarmEvent(Alarm.builder()
                .alarmType(alarmType)
                .targetUser(target)
                .fromUser(from)
                .build());
    }

    public Alarm getAlarm() {
        return alarm;
    }
}
