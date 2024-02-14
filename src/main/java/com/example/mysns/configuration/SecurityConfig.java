package com.example.mysns.configuration;


import com.example.mysns.jwt.JwtFilter;
import com.example.mysns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable() // ui 쪽 접근 차단
                .csrf().disable() // 크로스 사이트 기능?
                .cors().and()// 크로스 사이트 도메인이 다를때 허용?
                .authorizeRequests()
//                .antMatchers("/api/**").permitAll() // api/** 허용
                .antMatchers("/api/v1/users/join", "/api/v1/users/login").permitAll() // join, login 허용
                .antMatchers(HttpMethod.GET, "/api/v1/**").permitAll() // Get 허용
                .antMatchers(HttpMethod.GET, "/api/v1/posts/my").authenticated() // 인증 필요
                .antMatchers(HttpMethod.GET, "/api/v1/alarms").authenticated() // 인증 필요
                .antMatchers(HttpMethod.POST, "/api/v1/**").authenticated() // 인증 필요
                .antMatchers(HttpMethod.PUT, "/api/v1/**").authenticated() // 인증 필요
                .antMatchers(HttpMethod.PATCH, "/api/v1/**").authenticated() // 인증 필요
                .antMatchers(HttpMethod.DELETE, "/api/v1/**").authenticated() // 인증 필요
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt 사용하는 경우 씀
                .and()
                .addFilterBefore(new JwtFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}