package com.project.bridge.utils;

import com.project.bridge.dto.ResponseDto;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityUtil {
    
    public static ResponseEntity<ResponseDto> ok() {
        return ResponseEntity.ok(ResponseDto.builder().code(200).build());
    }
    
    public static ResponseEntity<ResponseDto> ok(String msg) {
        return ResponseEntity.ok(ResponseDto.builder().code(200).msg(msg).build());
    }
    
    public static ResponseEntity<ResponseDto> ok(Object body) {
        return ResponseEntity.ok(ResponseDto.builder().code(200).data(body).build());
    }
    
    public static ResponseEntity<ResponseDto> error(int code, String msg) {
        return ResponseEntity.ok(ResponseDto.builder().code(code).msg(msg).build());
    }
    
    public static ResponseEntity<ResponseDto> error(int code, Object body, @Nullable String msg) {
        return ResponseEntity.ok(ResponseDto.builder().code(code).data(body).msg(msg).build());
    }
    
    public static ResponseEntity unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
