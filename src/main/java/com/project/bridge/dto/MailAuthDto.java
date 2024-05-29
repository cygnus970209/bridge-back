package com.project.bridge.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailAuthDto {

    @NotNull
    private Integer idx;

    @NotNull(message = "확인코드는 필수입니다.")
    private Integer code;

    @NotNull(message = "이메일은 필수입니다.")
    private String email;

}
