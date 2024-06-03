package com.project.bridge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

public class UserResDto {
    
    @Getter
    @Builder
    public static class nicknameDuplicated {
        @JsonProperty("is_duplicated")
        private boolean isDuplicated;
    }
}
