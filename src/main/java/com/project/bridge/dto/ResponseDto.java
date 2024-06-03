package com.project.bridge.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseDto {
    private int code;
    private String msg;
    private Object data;
}
