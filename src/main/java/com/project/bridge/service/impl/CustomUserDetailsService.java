package com.project.bridge.service.impl;

import com.project.bridge.entity.MemberEntity;
import com.project.bridge.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity member = memberRepository.findByEmail(username);

        if (member == null) {{
            throw new UsernameNotFoundException("User not found with email: " + username);
        }}

        return org.springframework.security.core.userdetails.User.builder()
                .username(String.valueOf(member.getIdx()))
                .password(member.getPassword())
                .roles("USER")
                .build();
    }
}
