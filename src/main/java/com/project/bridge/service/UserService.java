package com.project.bridge.service;

import com.project.bridge.dto.UserDto;
import com.project.bridge.entity.MailAuthEntity;
import com.project.bridge.entity.RoleEntity;
import com.project.bridge.entity.UserEntity;

import java.security.NoSuchAlgorithmException;

public interface UserService {
    UserEntity save(UserDto.User userDto) throws NoSuchAlgorithmException;

    RoleEntity saveRole(Long userIdx) throws NoSuchAlgorithmException;

    MailAuthEntity saveMailAuth(Integer code, String email);

    boolean existByNickname(String nickname);

    Integer sendCodeToEmail(String to);

    boolean verifiedCode(String authIdx, String authNo);
}
