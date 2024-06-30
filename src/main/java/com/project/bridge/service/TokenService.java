package com.project.bridge.service;

import com.project.bridge.Exception.AuthException;
import com.project.bridge.domain.UserEntity;
import com.project.bridge.filter.Token;
import com.project.bridge.repository.TokenRepository;
import com.project.bridge.repository.UserRepository;
import com.project.bridge.security.type.JwtValidationType;
import com.project.bridge.utils.JwtUtil;
import com.project.bridge.utils.ResponseEntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    
    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    
    
    public ResponseEntity reissue(String accessToken, String refreshToken) {
    
        JwtValidationType jwtValidationType = jwtUtil.validateToken(refreshToken);
        if (jwtValidationType.equals(JwtValidationType.INVALID)) {
            throw new AuthException();
        }
    
        Long userIdx = jwtUtil.getUserIdx(accessToken);
        UserEntity userEntity = userRepository.findById(userIdx).orElse(null);
        if (userEntity == null) {
            throw new AuthException();
        }
    
        Token token = tokenRepository.findById(userIdx).orElseThrow(AuthException::new);
        if (!refreshToken.equals(token.getRefreshToken())) {
            tokenRepository.deleteById(userIdx);
            throw new AuthException();
        }
    
        String newAccessToken = jwtUtil.generateAccessToken(userIdx);
        String newRefreshToken = jwtUtil.generateRefreshToken();
    
        tokenRepository.save(Token.builder()
            .id(userIdx)
            .accessToken(newAccessToken)
            .refreshToken(newRefreshToken)
            .build());
        
        jwtUtil.setToken(newAccessToken, newRefreshToken);
    
        return ResponseEntityUtil.ok();
    }
}
