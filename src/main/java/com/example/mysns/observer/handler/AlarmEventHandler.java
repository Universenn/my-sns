package com.example.mysns.observer.handler;

import com.example.mysns.observer.events.AlarmEvent;
import com.example.mysns.repository.AlarmRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AlarmEventHandler {
    private final AlarmRepository alarmRepository;

    public AlarmEventHandler(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    @EventListener
    public void creatAlarm(AlarmEvent event) {
        alarmRepository.save(event.getAlarm());
    }
}
