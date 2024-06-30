package com.project.bridge.controller;

import com.project.bridge.dto.UserReqDto;
import com.project.bridge.service.UserService;
import com.project.bridge.utils.ValidationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @PostMapping("/mail-request")
    public ResponseEntity mailRequest(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        ValidationUtils.validateEmail(email);
        
        return userService.mailRequest(email);
    }
    
    @PostMapping("/mail-auth")
    public ResponseEntity mailAuth(@RequestBody @Valid UserReqDto.MailAuth request) {
        return userService.mailAuth(request);
    }
    
    @GetMapping("/dup")
    public ResponseEntity checkNicknameDuplicated(@RequestParam String nickname) {
        return userService.checkNicknameDuplicated(nickname);
    }
    
    @PostMapping("")
    public ResponseEntity save(@RequestBody UserReqDto.Create request) {
        ValidationUtils.validateEmail(request.getEmail());
        ValidationUtils.validateNickname(request.getNickname());
        ValidationUtils.validatePassword(request.getPassword());
        
        return userService.save(request);
    }
    
    @GetMapping("test1")
    public String test1(Authentication authentication) {
        return authentication.toString();
    }
}
