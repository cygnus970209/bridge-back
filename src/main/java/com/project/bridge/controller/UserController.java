package com.project.bridge.controller;

import com.project.bridge.dto.ResponseDto;
import com.project.bridge.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/mail-request")
    public ResponseEntity<ResponseDto> mailRequest() {
        return ResponseUtils.ok("메일 요청 성공");
    }

    @PostMapping("/mail-auth")
    public ResponseEntity<ResponseDto> mailAuth() {
        return ResponseUtils.ok("메일 인증 성공");
    }

    @GetMapping("/dup")
    public ResponseEntity<ResponseDto> getNickname() {
        return ResponseUtils.ok("유저 조회 성공");
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createUser() {
        return ResponseUtils.ok("유저 생성 성공");
    }
}
