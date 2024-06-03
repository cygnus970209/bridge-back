package com.project.bridge.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class UserReqDto {
    
    @Getter
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MailAuth {
    
        @NotNull(message = "인증번호를 입력해주세요.")
        private Long authIdx;
        @NotNull(message = "인증번호를 입력해주세요.")
        private Integer authNo;
    }
    
    @Getter
    @Builder
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Create {
        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;
        @NotBlank(message = "이름을 입력해주세요.")
        private String userName;
        @NotBlank(message = "닉네임을 입력해주세요.")
        private String nickname;
        @NotBlank(message = "비밀번호을 입력해주세요.")
        private String password;
    }
}
