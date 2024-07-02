package com.project.bridge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bridge.config.security.JwtTokenProvider;
import com.project.bridge.dto.MemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    private MemberDto.LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new MemberDto.LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");
    }

    @Test
    void testLogin() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtTokenProvider.createToken(any(Long.class), any(String.class))).thenReturn("accessToken", "refreshToken");

        mockMvc.perform(post("/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testRefreshToken() throws Exception {
        when(jwtTokenProvider.validateToken(any(String.class))).thenReturn(true);
        when(jwtTokenProvider.getUserId(any(String.class))).thenReturn(1L);
        when(jwtTokenProvider.createToken(any(Long.class), any(String.class))).thenReturn("newAccessToken", "newRefreshToken");

        mockMvc.perform(post("/v1/user/refresh")
                        .param("refreshToken", "validRefreshToken"))
                .andExpect(status().isOk());
    }
}
