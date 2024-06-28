package com.project.bridge.config.security;

import com.project.bridge.entity.UserEntity;
import com.project.bridge.repositories.support.RoleRepositorySupport;
import com.project.bridge.repositories.support.UserRepositorySupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepositorySupport userRepositorySupport;

    @Autowired
    private RoleRepositorySupport roleRepositorySupport;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        UserEntity userEntity = userRepositorySupport.findByEmail(userEmail);
        String role = roleRepositorySupport.findRoleByUserId(userEntity.getIdx());
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(role));

        return new UserAccount(userEntity, roles);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserEntity userEntity) {
        String role = roleRepositorySupport.findRoleByUserId(userEntity.getIdx());
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }
}
