package com.project.bridge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {

    @Getter
    @Setter
    @ToString
    public static class User {

        @NotNull(message = "이메일은 필수입니다.")
        @JsonProperty("user_email")
        private String userEmail;

        @NotNull(message = "이름은 필수입니다.")
        @JsonProperty("user_name")
        private String userName;

        @NotNull(message = "비밀번호는 필수입니다.")
        @JsonProperty("password")
        private String password;
    }

}
