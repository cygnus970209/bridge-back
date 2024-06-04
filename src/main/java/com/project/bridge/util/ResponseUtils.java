package com.project.bridge.util;

import com.project.bridge.dto.ResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ResponseUtils {
    public static ResponseEntity<ResponseDto> error(int code, String msg) {
        return ResponseEntity.ok(ResponseDto.builder().code(code).msg(msg).build());
    }

    public static ResponseEntity<ResponseDto> error(int code, Map<String, Object> body) {
        return ResponseEntity.ok(ResponseDto.builder().code(code).data(body).build());
    }

    public static ResponseEntity<ResponseDto> ok(String msg) {
        return ResponseEntity.ok(ResponseDto.builder().code(200).msg(msg).build());
    }

    public static ResponseEntity<ResponseDto> ok(Map<String, Object> body) {
        return ResponseEntity.ok(ResponseDto.builder().code(200).data(body).build());
    }
}
