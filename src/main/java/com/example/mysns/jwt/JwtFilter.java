package com.example.mysns.jwt;

import com.example.mysns.jwt.utils.JwtTokenUtil;
import com.example.mysns.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authentication : {}",authorization);

        if (authorization==null || !authorization.startsWith("Bearer ") ){
            log.error("authentication 이 없습니다.");
            filterChain. doFilter(request, response);
            return;
        }
        // Token 꺼내기

        String token = authorization.split(" ")[1];

        // Token Expired 되었는지 여부
        if (JwtTokenUtil.isExpired(token, secretKey)) {
            log.error("Token 이 만료 되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // userName Token 에서 꺼내기
        String userName = JwtTokenUtil.getUserName(token, secretKey);
        log.info("userName : {}", userName);

        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userName, null, List.of(new SimpleGrantedAuthority("USER")));

        // Detail 을 넣어줍니다.
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);

    }
}