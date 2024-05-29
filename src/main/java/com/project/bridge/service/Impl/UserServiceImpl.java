package com.project.bridge.service.Impl;

import com.project.bridge.dto.UserDto;
import com.project.bridge.repositories.UserRepository;
import com.project.bridge.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
@Qualifier("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean save(UserDto.User userDto) {



        return true;
    }

}
