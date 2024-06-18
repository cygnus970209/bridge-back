package com.project.bridge.security;

import com.project.bridge.Exception.AuthException;
import com.project.bridge.domain.UserEntity;
import com.project.bridge.domain.constant.Role;
import com.project.bridge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) {
    
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(() -> new AuthException("이메일을 확인해주세요."));
    
        return UserPrincipal.builder()
            .userIdx(userEntity.getUserIdx())
            .email(userEntity.getEmail())
            .password(userEntity.getPassword())
            .userName(userEntity.getUserName())
            .nickname(userEntity.getNickname())
            .role(Role.USER)
            .build();
    }
}
