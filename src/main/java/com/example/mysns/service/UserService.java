package com.example.mysns.service;

import com.example.mysns.dto.user.UserJoinRequest;
import com.example.mysns.dto.user.UserJoinResponse;
import com.example.mysns.dto.user.UserLoginRequest;
import com.example.mysns.dto.user.UserLoginResponse;
import com.example.mysns.entity.User;
import com.example.mysns.exception.AppException;
import com.example.mysns.exception.ErrorCode;
import com.example.mysns.repository.UserRepository;
import com.example.mysns.jwt.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String key;

    // 회원 가입
    public UserJoinResponse join(UserJoinRequest dto) {
        // 이메일 중복 확인
        userRepository.findByEmail(dto.getEmail()).ifPresent(user -> {
            throw new AppException(
                    ErrorCode.DUPLICATED_EMAIL,
                    ErrorCode.DUPLICATED_EMAIL.getMessage());
        });
        // 닉네임 중복 확인
        userRepository.findByNickname(dto.getNickname()).ifPresent(user -> {
            throw new AppException(
                    ErrorCode.DUPLICATED_NICKNAME,
                    ErrorCode.DUPLICATED_NICKNAME.getMessage());
        });

        String encoded = encoder.encode(dto.getPassword());
        User user = userRepository.save(dto.toEntity(encoded));

        return UserJoinResponse.of(user);
    }

    public UserLoginResponse login(UserLoginRequest dto) {
        // 해당 이메일 존재하지 않음
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> {
            throw new AppException(
                    ErrorCode.EMAIL_NOT_FOUND,
                    ErrorCode.EMAIL_NOT_FOUND.getMessage());
        });
        log.info("selectedPw : {}, pw : {}", user.getPassword(), dto.getPassword());
        if (!encoder.matches(dto.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, ErrorCode.INVALID_PASSWORD.getMessage());
        }
        String token = JwtTokenUtil.createToken(user.getEmail(), key);

        return new UserLoginResponse(token);
    }

}
