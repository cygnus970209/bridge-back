package com.project.bridge.service.Impl;

import com.project.bridge.config.security.ShaEncoder;
import com.project.bridge.dto.UserDto;
import com.project.bridge.entity.MailAuthEntity;
import com.project.bridge.entity.RoleEntity;
import com.project.bridge.entity.UserEntity;
import com.project.bridge.repositories.support.RoleRepositorySupport;
import com.project.bridge.repositories.support.UserRepositorySupport;
import com.project.bridge.service.MailService;
import com.project.bridge.service.RedisService;
import com.project.bridge.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;

@Service
@Transactional
@Slf4j
@Qualifier("userServiceImpl")
public class UserServiceImpl implements UserService {

    private static final String AUTH_CODE_PREFIX = "auth_code_";

    @Autowired
    private RedisService redisService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepositorySupport userRepositorySupport;

    @Autowired
    private RoleRepositorySupport roleRepositorySupport;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    @Value("${spring.mail.username}")
    private String username;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEntity save(UserDto.User userDto) throws NoSuchAlgorithmException {
        ShaEncoder shaEncoder = new ShaEncoder();
        //String encodePassword = shaEncoder.encrypt(userDto.getPassword());
        String encodePassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodePassword);
        return userRepositorySupport.save(userDto);
    }

    @Override
    public RoleEntity saveRole(Long userIdx) throws NoSuchAlgorithmException {
        return roleRepositorySupport.saveRole(userIdx);
    }

    @Override
    public MailAuthEntity saveMailAuth(Integer code, String email){
        return userRepositorySupport.saveMailAuth(code, email);
    }

    @Override
    public boolean existByNickname(String nickname){
        return userRepositorySupport.existsByNickname(nickname);
    }


    @Override
    public Integer sendCodeToEmail(String to) {
        this.checkDuplicatedEmail(to);
        String subject = "bridge test 이메일 인증 번호";
        Integer authCode = this.createCode();
        redisService.setValues(AUTH_CODE_PREFIX+to, authCode.toString(), Duration.ofMillis(this.authCodeExpirationMillis));
        mailService.sendEmail(username,to,subject,authCode.toString());
        return authCode;
    }

    public void checkDuplicatedEmail(String email) {
        UserEntity userEntity = userRepositorySupport.findByEmail(email);
        if (userEntity != null) {
            log.debug("User already exists with email: " + email);
        }
    }

    public Integer createCode(){
        int length = 6;
        try{
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();

            Integer ranInt = random.nextInt(1000000);
           /* for(int i = 0; i < length; i++){
                builder.append(random.nextInt(10));
            }*/
            return ranInt;
        }catch (NoSuchAlgorithmException e){
            log.debug("NoSuchAlgorithmException");
            return null;
        }
    }

    @Override
    public boolean verifiedCode(String authIdx, String authNo){
        this.checkDuplicatedEmail(authIdx);
        String redisAuthCode = redisService.getValue(AUTH_CODE_PREFIX+authIdx);
        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authNo);
        return authResult;
    }
}
