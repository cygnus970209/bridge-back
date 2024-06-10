package com.project.bridge.controller;

import com.project.bridge.dto.UserDto;
import com.project.bridge.dto.resp.ResponseDto;
import com.project.bridge.entity.UserEntity;
import com.project.bridge.service.UserService;
import jakarta.validation.Valid;
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
        UserEntity user = userService.save(userDto);
        userService.saveRole(user.getIdx());
        map.put("userInfo", user);

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
        boolean result = userService.existByNickname(nickname);

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
    public ResponseEntity sendCertificationMail(@RequestParam(value = "email") @Valid String email) {
        Integer authIdx = userService.sendCodeToEmail(email);
        HashMap<String,Integer> map = new HashMap<>();
        if(authIdx>0){
            map.put("auth_idx",authIdx);
            userService.saveMailAuth(authIdx, email);

            return ResponseEntity.ok(
                        ResponseDto.builder()
                            .code(200)
                            .msg("전송되었습니다!")
                            .data(map)
                            .build()
            );
        }
        return ResponseEntity.ok(
                ResponseDto.builder()
                        .code(5000)
                        .msg("전송 실패")
                        .build()
        );
    }

    //인증번호 검증(일단 idx대신 email로 검증 구현함)
    @PostMapping("/mail-auth")
    public ResponseEntity checkCertificationNum(@RequestParam("auth_no") String authNo, @RequestParam("auth_idx") String authIdx) {
        boolean result = userService.verifiedCode(authIdx, authNo);
        HashMap<String, Boolean> map = new HashMap<>();

        if(result){
            map.put("result",true);
            return ResponseEntity.ok(
                    ResponseDto.builder()
                            .code(200)
                            .msg("인증 성공")
                            .data(map)
                            .build()
            );
        }
        return ResponseEntity.ok(
                ResponseDto.builder()
                        .code(4003)
                        .msg("인증 실패")
                        .build()
        );
    }


}
