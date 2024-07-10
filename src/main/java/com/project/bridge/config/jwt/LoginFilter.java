package com.project.bridge.config.jwt;

import com.project.bridge.config.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    public LoginFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {

        String username = obtainUsername(req);
        String password = obtainPassword(req);

        //log.info("username:{}",username);
        //log.info("password:{}",password);

        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        //로그인 성공 시 실행
        //jwt
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String userEmail = customUserDetails.getUserEmail();
        Long userIdx = customUserDetails.getUserIdx();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();

        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();
        String token = tokenProvider.createToken(userIdx, userEmail, role);

        if(tokenProvider.validateToken(token)){
            res.addHeader("Authorization", "Bearer " + token);
            log.info("successful authentication");
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res, AuthenticationException failed) throws IOException, ServletException {
        //로그인 실패 시 실행
        log.info("unsuccessful authentication");
        res.setStatus(401);
    }
}

