package com.project.bridge.Exception;

import com.project.bridge.utils.ResponseEntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity missingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ResponseEntityUtil.error(4000, e.getMessage());
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity httpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntityUtil.error(4000, e.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, Object> body = new HashMap<>();
        Map<String, Object> validation = new HashMap<>();
        for (FieldError fieldError : e.getFieldErrors()) {
            validation.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        body.put("validation", validation);
        return ResponseEntityUtil.error(4000, body, "잘못된 요청입니다.");
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity validationException(ValidationException e) {
        return ResponseEntityUtil.error(4000, e.getValidation(), e.getMessage());
    }
    
    @ExceptionHandler(BizException.class)
    public ResponseEntity bizException(BizException e) {
        return ResponseEntityUtil.error(e.getCode(), e.getMessage());
    }
    
    @ExceptionHandler(AuthException.class)
    public ResponseEntity authException(AuthException e) {
        return ResponseEntityUtil.unauthorized();
    }
}
