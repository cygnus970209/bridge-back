package com.project.bridge.service;

import com.project.bridge.Exception.BizException;
import com.project.bridge.Exception.ValidationException;
import com.project.bridge.domain.MailAuthEntity;
import com.project.bridge.domain.UserEntity;
import com.project.bridge.dto.UserReqDto;
import com.project.bridge.dto.UserResDto;
import com.project.bridge.repository.MailAuthRepository;
import com.project.bridge.repository.UserRepository;
import com.project.bridge.utils.JwtUtil;
import com.project.bridge.utils.ResponseEntityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final MailAuthRepository mailAuthRepository;
    private final JwtUtil jwtUtil;
    
    public ResponseEntity mailRequest(String email) {
    
        Random random = new Random();
        int authNum = random.nextInt(899999) + 100000;
    
        MailAuthEntity mailAuthEntity = mailAuthRepository.save(MailAuthEntity.builder()
            .email(email)
            .authNo(String.valueOf(authNum))
            .build());
    
        StringBuilder text = new StringBuilder();
        text.append("[Bridge] 인증번호는 [ ");
        text.append(authNum);
        text.append(" ] 입니다.");
        
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom("lionskang@gmail.com");
        simpleMailMessage.setSubject("[Bridge] 인증번호 전송");
        simpleMailMessage.setText(text.toString());
    
        javaMailSender.send(simpleMailMessage);
    
        return ResponseEntityUtil.ok(UserResDto.mailRequestResDto.builder()
            .authIdx(mailAuthEntity.getIdx())
            .build());
    }
    
    @Transactional
    public ResponseEntity mailAuth(UserReqDto.MailAuth request) {
        MailAuthEntity mailAuth = mailAuthRepository.findByIdx(request.getAuthIdx());
        if(mailAuth == null) throw new ValidationException("authIdx", "올바르지 않은 인증번호입니다.");
    
        if (!mailAuth.getAuthNo().equals(request.getAuthNo())) {
            throw new BizException(4000, "올바르지 않은 인증번호입니다.");
        }
    
        mailAuth.setIsAuth(1);
        mailAuth.setAuthDt(LocalDateTime.now());
        
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
    
    public ResponseEntity save(UserReqDto.Create createDto) {
        if (userRepository.findByEmail(createDto.getEmail()).isPresent()) {
            throw new BizException(4001, "중복된 이메일 입니다.");
        }
        
        String encodedPassword = passwordEncoder.encode(createDto.getPassword());
        UserEntity user = UserEntity.builder()
            .email(createDto.getEmail())
            .password(encodedPassword)
            .userName(createDto.getUserName())
            .nickname(createDto.getNickname())
            .build();
        userRepository.save(user);
    
        jwtUtil.setToken(user);
        
        return ResponseEntityUtil.ok();
    }
}
