package com.project.bridge.utils;

import com.project.bridge.domain.UserEntity;
import com.project.bridge.security.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AuthorizationServiceException;
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
    private final long ACCESS_TOKEN_EXPIRATION = 86400L;
    
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    
    
    private String createToken(Long userIdx) {
        Claims claims = Jwts.claims();
        claims.put("userIdx", userIdx);
    
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime expiration = now.plusSeconds(ACCESS_TOKEN_EXPIRATION);
    
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date.from(now.toInstant()))
            .setExpiration(Date.from(expiration.toInstant()))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", token);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", token);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", token);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", token);
        }
        return false;
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
    
        String token = this.createToken(user.getUserIdx());
        response.addHeader("Authorization", "Bearer " + token);
    }
    
    public void setToken(UserPrincipal principal) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        
        String token = this.createToken(principal.getUserIdx());
        response.addHeader("Authorization", "Bearer " + token);
    }
    
    public String parseAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (!StringUtils.hasLength(authorization)) {
            if (authorization.startsWith("Bearer ") && authorization.length() > 7) {
                return authorization.substring(7);
            }
        }
        throw new AuthorizationServiceException("Not Authorized");
    }
    
}
