package com.project.bridge.service.Impl;

import com.project.bridge.config.security.ShaEncoder;
import com.project.bridge.dto.UserDto;
import com.project.bridge.entity.UserEntity;
import com.project.bridge.repositories.support.UserRepositorySupport;
import com.project.bridge.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
@Transactional
@Slf4j
@Qualifier("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepositorySupport userRepositorySupport;

    @Override
    public UserEntity save(UserDto.User userDto) throws NoSuchAlgorithmException {
        ShaEncoder shaEncoder = new ShaEncoder();
        String encodePassword = shaEncoder.encrypt(userDto.getPassword());
        userDto.setPassword(encodePassword);
        return userRepositorySupport.save(userDto);
    }

    @Override
    public boolean existByUserName(String userName){
        return userRepositorySupport.existsByUserName(userName);
    }

}
