package com.project.bridge.Exception;

import lombok.Getter;

@Getter
public class BizException extends RuntimeException{
    private int code;
    
    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }
    
}
