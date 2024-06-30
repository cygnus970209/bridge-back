package com.project.bridge.filter;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@RedisHash(value = "token", timeToLive = 1000L * 60 * 60)
public class Token {
    
    @Id
    private Long id; // userIdx
    private String accessToken;
    private String refreshToken;

}
