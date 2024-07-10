package com.project.bridge.config.jwt;

import com.project.bridge.config.security.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Slf4j
@Component
public class TokenProvider {

    //JWT토큰으로 유저정보를 암호화/복호화
    private static final long ACCESS_TOKEN_VALIDITY_SECONDS = 1000 * 60 * 60;       //1시간
    private static final long REFRESH_TOKEN_VALIDITY_SECONDS = 1000 * 60 * 60 * 24 * 7;     //1주일

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;

    private final CustomUserDetailsService customUserDetailsService;

    public TokenProvider(@Qualifier("customUserDetailsService") CustomUserDetailsService customUserDetailsService){
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostConstruct
    protected void init(){
        byte[] byteSecretKey = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(byteSecretKey);
    }

    //Jwt 토큰 생성
    public String createToken(Long userIdx, String userEmail, String role){
        Claims claims = Jwts.claims().setSubject(userIdx.toString());
        claims.put("userEmail", userEmail);
        claims.put("role", role);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+ REFRESH_TOKEN_VALIDITY_SECONDS))   //만료기간
                .signWith(key, SignatureAlgorithm.HS256)       //암호화 알고리즘
                .compact();    //token 생성
    }

    //토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

}
