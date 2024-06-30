package com.project.bridge.repository;

import com.project.bridge.filter.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TokenRepositoryTest {
    
    @Autowired
    TokenRepository tokenRepository;
    
    @DisplayName("Data Redis Test")
    @Test
    void t1() {
    
        tokenRepository.save(Token.builder()
            .id(11L)
            .accessToken("a1")
            .refreshToken("r1")
            .build());
    
        Token token = tokenRepository.findById(11L).orElse(null);
    
        Assertions.assertEquals("a1", token.getAccessToken());
        Assertions.assertEquals("r1", token.getRefreshToken());
    }

}