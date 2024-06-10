package com.project.bridge.repositories.support;

import com.project.bridge.config.querydsl.BridgeQueryDslRepositorySupport;
import com.project.bridge.config.security.ShaEncoder;
import com.project.bridge.dto.RoleDto;
import com.project.bridge.dto.UserDto;
import com.project.bridge.entity.MailAuthEntity;
import com.project.bridge.entity.RoleEntity;
import com.project.bridge.entity.UserEntity;
import com.project.bridge.repositories.MailRepository;
import com.project.bridge.repositories.RoleRepository;
import com.project.bridge.repositories.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.security.NoSuchAlgorithmException;
import static com.project.bridge.entity.QUserEntity.userEntity;

@Repository
@Slf4j
public class UserRepositorySupport extends BridgeQueryDslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailRepository mailRepository;

    public UserRepositorySupport(@Qualifier("bridgeJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(UserEntity.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    //회원가입 시 사용자 저장
    public UserEntity save(UserDto.User userDto) throws NoSuchAlgorithmException {
        return userRepository.save(UserEntity.builder()
                .nickname(userDto.getNickname())
                .userEmail(userDto.getUserEmail())
                .password(userDto.getPassword()).build());
    }

    //회원가입 시 권한 저장
    public RoleEntity saveRole(Long userIdx) throws NoSuchAlgorithmException {
        return roleRepository.save(RoleEntity.builder()
                        .userIdx(userIdx)
                        .role("USER")
                        .build());
    }

    //닉네임 중복확인
    public boolean existsByNickname(String nickname) {
        return jpaQueryFactory
                .selectFrom(userEntity)
                .where(userEntity.nickname.eq(nickname))
                .fetchFirst() != null;
    }


    //이메일 중복 확인
    public UserEntity findByEmail(String email){
        return jpaQueryFactory
                .selectFrom(userEntity)
                .where(userEntity.userEmail.eq(email))
                .fetchFirst();
    }

    //인증메일 발송 저장
    public MailAuthEntity saveMailAuth(Integer code, String email){
        return mailRepository.save(MailAuthEntity.builder()
                        .code(code)
                        .email(email)
                        .build());
    }

    //인증번호 검증

}
