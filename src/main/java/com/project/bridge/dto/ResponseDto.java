package com.project.bridge.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ResponseDto {
    private Integer code;

    private String msg;

    private Map<String, Object> data;
}
