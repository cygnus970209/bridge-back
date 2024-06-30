package com.project.bridge.utils;

import com.project.bridge.domain.UserEntity;
import com.project.bridge.security.type.JwtValidationType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;


@Slf4j
@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String SECRET;
    
    private final Key key;
    private final long ACCESS_TOKEN_EXPIRATION_SECOND = 1000L * 60 * 5; //
    private final long REFRESH_TOKEN_EXPIRATION_SECOND = 1000L * 60 * 60;
    
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    
    
    public String generateAccessToken(Long userIdx) {
        Claims claims = Jwts.claims();
        claims.put("userIdx", userIdx);
    
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expiration = now.plusSeconds(ACCESS_TOKEN_EXPIRATION_SECOND);
    
        return Jwts.builder()
            .setIssuer("bridge")
            .setClaims(claims)
            .setIssuedAt(Date.from(now.toInstant()))
            .setExpiration(Date.from(expiration.toInstant()))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }
    
    public String generateRefreshToken() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expiration = now.plusSeconds(ACCESS_TOKEN_EXPIRATION_SECOND);
    
        return Jwts.builder()
            .setIssuer("bridge")
            .setIssuedAt(Date.from(now.toInstant()))
            .setExpiration(Date.from(expiration.toInstant()))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }
    
    public JwtValidationType validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return JwtValidationType.VALID;
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", token);
            return JwtValidationType.EXPIRED;
        } catch (Exception e) {
            log.info("Invalid JWT Token", token);
            return JwtValidationType.INVALID;
        }
    }
    
    public Long getUserIdx(String accessToken) {
        return parseClaims(accessToken).get("userIdx", Long.class);
    }
    
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
    
    public void setToken(UserEntity user) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    
        String AccessToken = this.generateAccessToken(user.getUserIdx());
        response.addHeader("Authorization", "Bearer " + AccessToken);
    }
    
    public void setToken(String accessToken, String refreshToken) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("RefreshToken", refreshToken);
    }
    
    public String parseAccessToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorization = request.getHeader("Authorization");
        if (StringUtils.hasLength(authorization)) {
            if (authorization.startsWith("Bearer ") && authorization.length() > 7) {
                return authorization.substring(7);
            }
        }
        return null;
    }
    
    public String parseRefreshToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String refreshToken = request.getHeader("RefreshToken");
        return refreshToken;
    }
    
}
