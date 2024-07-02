package com.project.bridge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class MemberDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MailRequest {
        @Email(message = "이메일 형식이 아닙니다.")
        @NotEmpty(message = "이메일은 필수값입니다.")
        private String email;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CheckMailRequest {
        @NotNull(message = "인증번호는 필수값입니다.")
        private Long authIdx;

        @NotEmpty(message = "인증번호는 필수값입니다.")
        private String authNo;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DupNicknameRequest {
        @NotEmpty(message = "닉네임는 필수값입니다.")
        private String nickname;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SaveMemberRequest {
        @Email
        @NotEmpty(message = "이메일은 필수값입니다.")
        @JsonProperty("email")
        private String email;

        @NotEmpty(message = "닉네임는 필수값입니다.")
        @JsonProperty("nickname")
        private String nickname;

        @NotEmpty(message = "비밀번호 필수값입니다.")
        @JsonProperty("password")
        private String password;
    }

    @Getter
    @Setter
    public static class LoginRequest {
        @NotEmpty(message = "이메일은 필수값입니다.")
        private String email;

        @NotEmpty(message = "비밀번호는 필수값입니다.")
        private String password;
    }

    @Getter
    @Setter
    public static class RefreshRequest {
        private String token;
    }
}
