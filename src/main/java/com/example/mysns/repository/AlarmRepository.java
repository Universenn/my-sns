package com.example.mysns.repository;

import com.example.mysns.entity.Alarm;
import com.example.mysns.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Page<Alarm> findByTargetUser(User user, Pageable pageable);
}
