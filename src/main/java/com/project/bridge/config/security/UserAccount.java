package com.project.bridge.config.security;

import com.project.bridge.entity.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

//사용자 정보 담는 인터페이스
@Getter
public class UserAccount extends User {

    private final UserEntity userEntity;

    public UserAccount(UserEntity userEntity, Collection<? extends GrantedAuthority> authorities) {
        super(userEntity.getUserEmail(), userEntity.getPassword(), authorities);
        this.userEntity = userEntity;
    }

}
