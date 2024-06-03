package com.project.bridge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bridge.domain.UserEntity;
import com.project.bridge.dto.UserReqDto;
import com.project.bridge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@ActiveProfiles("test")
class UserControllerTest {
    
    @Autowired
    MockMvc mockMvc;
    
    @Autowired
    ObjectMapper objectMapper;
    
    @Autowired
    UserRepository userRepository;
    
    
    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }
    
    @Test
    @DisplayName("메일전송")
    void t1() throws Exception {
        // given
        Map<String, String> body = new HashMap<>();
        body.put("email", "kangcp@intocns.com");

        // expected
        mockMvc.perform(post("/v1/user/mail-request")
                    .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andDo(print());
    }
    
    @Test
    @DisplayName("메일전송_유효성검사")
    void t2() throws Exception {
        // exception
        mockMvc.perform(post("/v1/user/mail-request")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(4000))
            .andDo(print());
    }
    
    @Test
    @DisplayName("메일전송_유효성검사")
    void t3() throws Exception {
        // given
        Map<String, String> body = new HashMap<>();
        body.put("email", "kangcp.com");
    
        // expected
        mockMvc.perform(post("/v1/user/mail-request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(4000))
            .andDo(print());
    }
    
    @Test
    @DisplayName("메일 인증번호 검증")
    void t4() throws Exception {
        // given
        Map<String, Object> body = new HashMap<>();
        body.put("auth_no", 1);
        body.put("auth_idx", 1);
        
        // expected
        mockMvc.perform(post("/v1/user/mail-auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andDo(print());
    }
    
    @Test
    @DisplayName("메일 인증번호 검증_유효성검사")
    void t5() throws Exception {
        // given
        Map<String, Object> body = new HashMap<>();
        body.put("auth_no", 1);
        
        // expected
        mockMvc.perform(post("/v1/user/mail-auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(4000))
            .andExpect(jsonPath("$.data.validation.authNo").doesNotExist())
            .andExpect(jsonPath("$.data.validation.authIdx").isNotEmpty())
            .andDo(print());
    }
    
    @Test
    @DisplayName("닉네임 중복확인")
    void t6() throws Exception {
        // given
        // expected
        mockMvc.perform(get("/v1/user/dup?nickname={}", "nickname")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andDo(print());
    }
    
    @Test
    @DisplayName("닉네임 중복확인_유효성검사")
    void t7() throws Exception {
        // given
        // expected
        mockMvc.perform(get("/v1/user/dup" )
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(4000))
            .andDo(print());
    }
    
    @Test
    @DisplayName("회원정보 저장")
    void t8() throws Exception {
        // given
        UserReqDto.Create body = UserReqDto.Create.builder()
            .email("kangcp@intocns.com")
            .nickname("kangcp")
            .password("kangcp1234")
            .build();
    
        // expected
        mockMvc.perform(post("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andDo(print());
    }
    
    @Test
    @DisplayName("회원정보 저장_유효성검사")
    void t9() throws Exception {
        // given
        UserReqDto.Create body = UserReqDto.Create.builder()
            .email("")
            .nickname("")
            .password("")
            .build();
        
        // expected
        mockMvc.perform(post("/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(4000))
            .andDo(print());
    }
    
    @Test
    @DisplayName("닉네임 중복확인")
    void t10() throws Exception {
        // given
        UserEntity user = UserEntity.builder()
            .email("kangcp@intocns.com")
            .nickname("kangcp")
            .userName("kangcp")
            .password("kangcp")
            .build();
    
        userRepository.save(user);
        
        // expected
        mockMvc.perform(get("/v1/user/dup?nickname={nickname}", "kangcp1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data.is_duplicated").value(false))
            .andDo(print());
        
        mockMvc.perform(get("/v1/user/dup?nickname={nickname}", "kangcp")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.code").value(4000))
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data.is_duplicated").value(true))
            .andDo(print());
    }
    
    
}