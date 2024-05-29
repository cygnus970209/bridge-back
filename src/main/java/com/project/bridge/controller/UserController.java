package com.project.bridge.controller;

import com.project.bridge.dto.UserDto;
import com.project.bridge.repositories.UserRepository;
import com.project.bridge.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    /**
     * HTTP STATUS CODE => Always 200(OK)
     * "code" : 200 or 4xxx or 5xxx, //int
     * "msg" : "내용",                //프론트에서 바로 사용할 수 있는 메세지 형태로..
     * "data" : Map<String, Object>
     *
     */


    //회원정보 저장
    @PostMapping
    public ResponseEntity createUser(@RequestBody UserDto.User userDto) {

        boolean result=true;

        return ResponseEntity.ok(result);
    }


    //인증메일 발송
    @PostMapping("/mail-request")
    public ResponseEntity sendCertificationMail(@RequestParam("email") String email) {

        String auth_idx="";
        return ResponseEntity.ok(auth_idx);
    }

    //인증번호 검증
    @PostMapping("/mail-auth")
    public ResponseEntity checkCertificationNum(@RequestParam("auth_no") String authNo, @RequestParam("auth_idx") String authIdx) {

        boolean result=true;
        return ResponseEntity.ok(result);
    }

    //닉네임 중복체크
    @GetMapping("/dup")
    public ResponseEntity checkDupNickName(@RequestParam("nickname") String nickname) {
        boolean result=true;
        return ResponseEntity.ok(result);
    }





}
