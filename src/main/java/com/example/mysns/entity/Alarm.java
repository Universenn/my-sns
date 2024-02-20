package com.example.mysns.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @ManyToOne
    private User targetUser;

    @ManyToOne
    private User fromUser;

    @CreatedDate
    private LocalDateTime createdDateTime;


    @Builder
    public Alarm(Long id, AlarmType alarmType, User targetUser, User fromUser) {
        this.id = id;
        this.alarmType = alarmType;
        this.targetUser = targetUser;
        this.fromUser = fromUser;
    }
}
