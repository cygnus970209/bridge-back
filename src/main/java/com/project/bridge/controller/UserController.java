package com.project.bridge.controller;

import com.google.api.client.util.Maps;
import com.project.bridge.config.security.JwtTokenProvider;
import com.project.bridge.dto.MemberDto;
import com.project.bridge.dto.ResponseDto;
import com.project.bridge.dto.TokenResponseDto;
import com.project.bridge.service.MemberService;
import com.project.bridge.util.ResponseUtils;
import com.project.bridge.util.SHA256Utils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    @Qualifier("memberServiceImpl")
    private MemberService memberService;

    @GetMapping("/mail-request")
    public ResponseEntity<ResponseDto> mailRequest(@Valid @ModelAttribute MemberDto.MailRequest mailRequest) {
        log.info("mailRequest: {}", mailRequest.getEmail());


        try {
            Long authIdx = memberService.sendAuthMail(mailRequest.getEmail());

            Map<String, Object> data = Maps.newHashMap();
            data.put("authIdx", authIdx);

            return ResponseUtils.ok(data);
        } catch (RuntimeException e) {
            log.error("메일 요청 실패", e);
            return ResponseUtils.error(500, "메일 요청 실패");
        }
    }

    @PostMapping("/mail-auth")
    public ResponseEntity<ResponseDto> mailAuth(@Valid MemberDto.CheckMailRequest mailAuth) {
        if (memberService.checkAuthMail(mailAuth.getAuthIdx(), mailAuth.getAuthNo())) {
            return ResponseUtils.ok("메일 인증 성공");
        } else {
            return ResponseUtils.error(400, "메일 인증 실패");
        }
    }

    @GetMapping("/dup")
    public ResponseEntity<ResponseDto> getNickname(@Valid @ModelAttribute MemberDto.DupNicknameRequest dupNicknameRequest) {
        if (memberService.dupNickname(dupNicknameRequest.getNickname())) {
            return ResponseUtils.ok("닉네임 중복 없음");
        } else {
            return ResponseUtils.error(400, "닉네임 중복 있음");
        }
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createUser(@Valid @RequestBody MemberDto.SaveMemberRequest saveMemberRequest) {
        memberService.saveMember(saveMemberRequest.getEmail(), saveMemberRequest.getPassword(), saveMemberRequest.getNickname());

        return ResponseUtils.ok("유저 생성 성공");
    }


    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody MemberDto.LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Long userId = Long.parseLong(authentication.getName());
        String accessToken = jwtTokenProvider.createToken(userId, loginRequest.getEmail(), "access");
        String refreshToken = jwtTokenProvider.createToken(userId, loginRequest.getEmail(), "refresh");

        TokenResponseDto tokenResponse = new TokenResponseDto(accessToken, refreshToken);

        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(@RequestBody MemberDto.RefreshRequest refreshToken) {
        if (jwtTokenProvider.validateToken(refreshToken.getToken())) {
            Long userId = jwtTokenProvider.getUserId(refreshToken.getToken());
            String userEmail = jwtTokenProvider.getUserEmail(refreshToken.getToken());
            String newAccessToken = jwtTokenProvider.createToken(userId, userEmail, "access");
            String newRefreshToken = jwtTokenProvider.createToken(userId, userEmail, "refresh");

            TokenResponseDto tokenResponse = new TokenResponseDto(newAccessToken, newRefreshToken);

            return ResponseEntity.ok(tokenResponse);
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }
}
