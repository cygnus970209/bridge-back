package com.project.bridge.controller;

import com.project.bridge.Exception.ValidationException;
import com.project.bridge.service.TokenService;
import com.project.bridge.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    
    @GetMapping("/reissue")
    
    public ResponseEntity reissue() {
    
        String accessToken = jwtUtil.parseAccessToken();
        String refreshToken = jwtUtil.parseRefreshToken();
        if (!StringUtils.hasLength(accessToken)) {
            throw new ValidationException("accessToken", "AccessToken Required.");
        }
        if (!StringUtils.hasLength(refreshToken)) {
            throw new ValidationException("refreshToken", "RefreshToken Required.");
        }
    
        return tokenService.reissue(accessToken, refreshToken);
    }
    
}
