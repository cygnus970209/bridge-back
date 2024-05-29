package com.project.bridge.repositories.support;

import com.project.bridge.config.querydsl.BridgeQueryDslRepositorySupport;
import com.project.bridge.entity.UserEntity;
import com.project.bridge.repositories.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import static com.project.bridge.entity.QUserEntity.userEntity;

@Repository
@Slf4j
public class UserRepositorySupport extends BridgeQueryDslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    private UserRepository userRepository;

    public UserRepositorySupport(@Qualifier("bridgeJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(UserEntity.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    //회원가입 시 사용자 저장
    public UserEntity save(UserEntity entity) {
        return userRepository.save(entity);
    }

    //이메일 중복확인
    public boolean existByUserEmail(String email) {
        return jpaQueryFactory
                .selectFrom(userEntity)
                .where(userEntity.userEmail.eq(email))
                .fetchFirst() != null;
    }



}
