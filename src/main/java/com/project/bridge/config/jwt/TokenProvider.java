package com.project.bridge.config.jwt;

import com.project.bridge.config.security.CustomUserDetailsService;
import com.project.bridge.dto.RoleDto;
import com.project.bridge.dto.auth.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    //JWT토큰으로 유저정보를 암호화/복호화
    private static final String AUTHORITIES_KEY = "auth";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final long ACCESS_TOKEN_VALIDITY_SECONDS = 1000 * 60 * 60;       //1시간
    private static final long REFRESH_TOKEN_VALIDITY_SECONDS = 1000 * 60 * 60 * 24 * 7;     //1주일

    @Value("${jwt.secret}")
    private String secretKey;

    private final CustomUserDetailsService customUserDetailsService;


    public TokenProvider(@Qualifier("customUserDetailsService") CustomUserDetailsService customUserDetailsService){
        this.customUserDetailsService = customUserDetailsService;
    }

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //Jwt 토큰 생성
    public String createToken(String userIdx, String role){
        Claims claims = Jwts.claims().setSubject(userIdx);
        claims.put("role", role);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+ REFRESH_TOKEN_VALIDITY_SECONDS))   //만료기간
                .signWith(SignatureAlgorithm.HS512,secretKey)       //암호화 알고리즘
                .compact();    //token 생성
    }


    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("X-Auth-Token");
    }

    public String getUserIdx(String token){
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
       /* try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }*/
    }
    //토큰 인증 성공 시 SecurityContextHolder에 저장할 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(this.getUserIdx(token));


       /* Claims claims = parseClaims(token);*/
       /* if(claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("Invalid access token");
        }*/
/*
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);*/
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

   /* //Access Token, Refresh Token 생성(암호화)
    public TokenDto generateTokenDto(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_VALIDITY_SECONDS * 1000);

        // Access Token
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.ES256)
                .compact();

        //Refresh Token
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_VALIDITY_SECONDS))
                .signWith(key, SignatureAlgorithm.ES256)
                .compact();

        return TokenDto.builder()
                .grantType(TOKEN_PREFIX)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

   */

    //토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
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
