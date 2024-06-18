package com.project.bridge.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bridge.Exception.AuthException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    
    public CustomAuthenticationFilter() {
        super("/v1/login");
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    
        if (!"POST".equals(request.getMethod())) {
            throw new IllegalStateException();
        }
    
        LoginDto loginDto = new ObjectMapper().readValue(request.getReader(), LoginDto.class);
    
        if (!StringUtils.hasLength(loginDto.email)) {
            throw new AuthException("이메일을 확인해주세요.");
        }
        if (!StringUtils.hasLength(loginDto.password)) {
            throw new AuthException("비밀번호를 확인해주세요.");
        }
    
        AuthenticationManager authenticationManager = getAuthenticationManager();
        return authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginDto.email, loginDto.password));
    }
    
    @Getter
    @Setter
    public static class LoginDto {
        private String email;
        private String password;
    }
}
