package com.project.bridge.config.jwt;

import com.nimbusds.jwt.JWT;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/*
import static org.springframework.security.config.Elements.JWT;*/
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private TokenProvider tokenProvider;

   /* public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";*/

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = tokenProvider.resolveToken(request);

        System.out.println("token:"+token);
        if (token != null && tokenProvider.validateToken(token)) {              //token 검증
            Authentication auth = tokenProvider.getAuthentication(token);       // 인증 객체 생성
            SecurityContextHolder.getContext().setAuthentication(auth);         //SecurityContextHolder에 인증
        }
        filterChain.doFilter(request, response);
    }
}

