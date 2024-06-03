package com.project.bridge.utils;

import com.project.bridge.Exception.ValidationException;

import java.util.regex.Pattern;

public class ValidationUtils {
    
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    
    public static void validateEmail(String email) {
        if(EMAIL_PATTERN.matcher(email).matches()) return;
        throw new ValidationException("email", "올바르지 않은 이메일 입니다.");
    }
    
    public static void validateNickname(String nickname) {
        // TODO 닉네임 정책
    }
    
    public static void validatePassword(String password) {
        // TODO 패스워드 정책
        
    }
}
