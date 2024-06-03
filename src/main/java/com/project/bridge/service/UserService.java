package com.project.bridge.service;

import com.project.bridge.dto.UserDto;
import com.project.bridge.entity.UserEntity;

import java.security.NoSuchAlgorithmException;

public interface UserService {
    UserEntity save(UserDto.User userDto) throws NoSuchAlgorithmException;

    boolean existByUserName(String userName);
}
