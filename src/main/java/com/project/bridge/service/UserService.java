package com.project.bridge.service;

import com.project.bridge.dto.UserDto;

public interface UserService {
    boolean save(UserDto.User userDto);
}
