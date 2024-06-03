package com.project.bridge.controller;

import com.project.bridge.dto.UserDto;
import com.project.bridge.dto.resp.ResponseDto;
import com.project.bridge.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/v1/user")
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
    public ResponseEntity createUser(@RequestBody UserDto.User userDto) throws NoSuchAlgorithmException {
        Map<String,Object> map = new HashMap<>();
        map.put("userInfo",userService.save(userDto));
        return ResponseEntity.ok(
                ResponseDto.builder()
                        .code(200)
                        .msg("가입 성공")
                        .data(map)
                        .build()
        );
    }

    //닉네임 중복체크
    @GetMapping("/dup")
    public ResponseEntity checkDupNickName(@RequestParam("nickname") String nickname) {
        boolean result = userService.existByUserName(nickname);

        //true=> 닉네임 있음, false => 닉네임 없음(사용가능)
        if(result){
            return ResponseEntity.ok(
                    ResponseDto.builder()
                            .code(4000)
                            .msg("사용 불가능한 닉네임입니다.")
                            .build()
            );
        }

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .code(200)
                        .msg("사용 가능한 닉네임입니다.")
                        .build()
        );
    }

    //인증메일 발송
    @PostMapping(value = "/mail-request")
    public ResponseEntity sendCertificationMail(@RequestParam(value = "email") String email) {
        String auth_idx="12345";
        return ResponseEntity.ok(auth_idx);
    }

    //인증번호 검증
    @PostMapping("/mail-auth")
    public ResponseEntity checkCertificationNum(@RequestParam("auth_no") String authNo, @RequestParam("auth_idx") String authIdx) {

        boolean result=true;
        return ResponseEntity.ok(result);
    }





}
