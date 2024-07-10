package com.project.bridge.config.security;

import com.project.bridge.entity.UserEntity;
import com.project.bridge.repositories.support.RoleRepositorySupport;
import com.project.bridge.repositories.support.UserRepositorySupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepositorySupport userRepositorySupport;

    @Autowired
    private RoleRepositorySupport roleRepositorySupport;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        UserEntity userEntity = userRepositorySupport.findByEmail(userEmail);
        if (userEntity == null) {
            throw new UsernameNotFoundException(userEmail);
        }
        String role = roleRepositorySupport.findRoleByUserId(userEntity.getIdx());

        return new CustomUserDetails(userEntity, role);
    }

}
