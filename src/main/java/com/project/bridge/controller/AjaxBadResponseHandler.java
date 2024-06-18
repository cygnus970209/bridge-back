package com.project.bridge.controller;

import com.project.bridge.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AjaxBadResponseHandler {

    @ExceptionHandler({BindException.class})
    public ResponseEntity<ResponseDto> validBindException(BindException e) {
        log.error("validBindException", e);
        return ResponseEntity.ok(ResponseDto.builder()
                .code(4000)
                .msg("파라미터 오류")
                .build());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseDto> validException(MethodArgumentNotValidException e) {
        log.error("validException", e);
        return ResponseEntity.ok(ResponseDto.builder()
                .code(4000)
                .msg("파라미터 오류")
                .build());
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ResponseDto> exception(Exception e) {
        log.error("exception", e);
        return ResponseEntity.ok(ResponseDto.builder()
                .code(5000)
                .msg("서버 오류")
                .build());
    }
}
