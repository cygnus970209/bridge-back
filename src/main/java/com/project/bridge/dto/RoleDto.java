package com.project.bridge.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoleDto {

    @NotNull
    private Integer idx;

    @NotNull(message = "권한은 필수입니다.")
    private String role;

}
