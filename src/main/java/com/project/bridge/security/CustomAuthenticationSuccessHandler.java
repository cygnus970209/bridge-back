package com.project.bridge.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bridge.dto.ResponseDto;
import com.project.bridge.filter.Token;
import com.project.bridge.repository.TokenRepository;
import com.project.bridge.utils.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        
        String accessToken = jwtUtil.generateAccessToken(principal.getUserIdx());
        String refreshToken = jwtUtil.generateRefreshToken();
    
        tokenRepository.save(Token.builder()
            .id(principal.getUserIdx())
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build());
        
        jwtUtil.setToken(accessToken, refreshToken);
        
        ObjectMapper objectMapper = new ObjectMapper();
        
        ResponseDto body = ResponseDto.builder()
            .code(200)
            .build();
        
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), body);
        
    }
}
