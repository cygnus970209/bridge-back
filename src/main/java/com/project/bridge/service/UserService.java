package com.project.bridge.service;

import com.project.bridge.Exception.BizException;
import com.project.bridge.domain.UserEntity;
import com.project.bridge.dto.UserReqDto;
import com.project.bridge.dto.UserResDto;
import com.project.bridge.repository.UserRepository;
import com.project.bridge.utils.ResponseEntityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    
    public ResponseEntity mailRequest(String email) {
        return ResponseEntityUtil.ok();
    }
    
    public ResponseEntity mailAuth(UserReqDto.MailAuth request) {
        return ResponseEntityUtil.ok();
    }
    
    public ResponseEntity checkNicknameDuplicated(String nickname) {
        // TODO 어떤 방식?
        
//        if (userRepository.findByNickname(nickname).isPresent()) {
//            throw new BizException(4000, "중복된 닉네임 입니다.");
//        }
//        return ResponseEntityUtil.ok();
    
        boolean isDuplicated = userRepository.findByNickname(nickname).isPresent();
    
        return ResponseEntityUtil.ok(UserResDto.nicknameDuplicated.builder()
            .isDuplicated(isDuplicated)
            .build());
    }
    
    public ResponseEntity save(UserReqDto.Create request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BizException(4001, "중복된 이메일 입니다.");
        }
        
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        UserEntity user = UserEntity.builder()
            .email(request.getEmail())
            .password(encodedPassword)
            .userName(request.getUserName())
            .nickname(request.getNickname())
            .build();
        userRepository.save(user);
    
        return ResponseEntityUtil.ok();
    }
}
