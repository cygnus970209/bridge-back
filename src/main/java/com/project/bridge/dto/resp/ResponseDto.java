package com.project.bridge.dto.resp;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ResponseDto {


    @Builder
    public ResponseDto(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private int code;
    private String msg;
    private Object data;
}
