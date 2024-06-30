package com.project.bridge.service;

import com.project.bridge.Exception.BizException;
import com.project.bridge.domain.MailAuthEntity;
import com.project.bridge.domain.UserEntity;
import com.project.bridge.dto.UserReqDto;
import com.project.bridge.repository.MailAuthRepository;
import com.project.bridge.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
class UserServiceTest {
    
    @Autowired
    UserService userService;
    
    @Autowired
    UserRepository userRepository;
    @Autowired
    MailAuthRepository mailAuthRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @BeforeEach
    void clean() {
        userRepository.deleteAll();
        mailAuthRepository.deleteAll();
    }
    
    
    @Test
    @DisplayName("회원가입")
    void t1() {
        // given
        UserReqDto.Create req = UserReqDto.Create.builder()
            .email("kangcp@into.com")
            .password("kangcp")
            .userName("username")
            .nickname("nickname")
            .build();
        
        // when
        userService.save(req);
        
        // then
        UserEntity user = userRepository.findAll().get(0);
        
        assertEquals(1L, userRepository.count());
        assertEquals("kangcp@into.com", user.getEmail());
        assertEquals(true, passwordEncoder.matches("kangcp", user.getPassword()));
    }
    
    
    @Test
    @DisplayName("회원가입_유효성검사")
    void t2() {
        // given
        UserReqDto.Create req = UserReqDto.Create.builder()
            .email("kangcp@into.com")
            .password("kangcp")
            .userName("username")
            .nickname("nickname")
            .build();
        
        UserReqDto.Create req1 = UserReqDto.Create.builder()
            .email("kangcp@into.com")
            .password("kangcp")
            .userName("username")
            .nickname("nickname")
            .build();
        
        // when
        assertThrows(BizException.class, () -> {
            userService.save(req);
            userService.save(req1);
        });
        
        // then
        UserEntity user = userRepository.findAll().get(0);
        assertEquals(1L, userRepository.count());
        assertEquals("kangcp@into.com", user.getEmail());
        assertEquals(true, passwordEncoder.matches("kangcp", user.getPassword()));
        
    }
    
    
    @Test
    @DisplayName("인증 메일 요청 및 인증")
    void t3() {
        userService.mailRequest("lionskang@gmail.com");
    
        MailAuthEntity mailAuthEntity = mailAuthRepository.findAll().get(0);
    
        Assertions.assertThrows(BizException.class, () -> {
            userService.mailAuth(UserReqDto.MailAuth.builder()
                .authIdx(mailAuthEntity.getIdx())
                .authNo("112233")
                .build());
        });
    
        Assertions.assertEquals(0, mailAuthRepository.findByIdx(mailAuthEntity.getIdx()).getIsAuth());
        
        userService.mailAuth(UserReqDto.MailAuth.builder()
            .authIdx(mailAuthEntity.getIdx())
            .authNo(mailAuthEntity.getAuthNo())
            .build());
    
        Assertions.assertEquals(1, mailAuthRepository.findByIdx(mailAuthEntity.getIdx()).getIsAuth());
    }
    
}