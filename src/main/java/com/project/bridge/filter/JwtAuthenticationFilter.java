package com.project.bridge.filter;

import com.project.bridge.repository.TokenRepository;
import com.project.bridge.security.CustomUserDetailService;
import com.project.bridge.security.type.JwtValidationType;
import com.project.bridge.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtUtil jwtUtil;
    private final CustomUserDetailService userDetailService;
    private final TokenRepository tokenRepository;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        // token 추출
        String accessToken = jwtUtil.parseAccessToken();
        
        // token이 없으면 filter 진행
        if (!StringUtils.hasLength(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }
    
        JwtValidationType jwtValidationType = jwtUtil.validateToken(accessToken);
    
        if (!jwtValidationType.equals(JwtValidationType.VALID)) {
            log.info("Invalid or Expired Token => ", accessToken);
            filterChain.doFilter(request, response);
            return;
        }
    
        Long userIdx = jwtUtil.getUserIdx(accessToken);
    
        Token token = tokenRepository.findById(userIdx).orElse(null);
        if (token == null || !token.getAccessToken().equals(accessToken)) {
            tokenRepository.deleteById(userIdx);
            filterChain.doFilter(request, response);
            return;
        }
    
        UserDetails userDetails = userDetailService.loadUserByUsername(userIdx);
    
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request, response);
    }
}
