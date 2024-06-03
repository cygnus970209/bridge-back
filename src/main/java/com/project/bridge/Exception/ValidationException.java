package com.project.bridge.Exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ValidationException extends RuntimeException{
    
    private static final String MESSAGE = "잘못된 요청입니다.";
    
    private Map<String, String> validation = new HashMap<>();
    
    public ValidationException(String message) {
        super(MESSAGE);
    }
    
    public ValidationException(String field, String errorMessage) {
        super(MESSAGE);
        validation.put(field, errorMessage);
    }
}
