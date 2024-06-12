package com.project.bridge.repositories.support;

import com.project.bridge.config.querydsl.BridgeQueryDslRepositorySupport;
import com.project.bridge.entity.RoleEntity;
import com.project.bridge.entity.UserEntity;
import com.project.bridge.repositories.RoleRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.security.NoSuchAlgorithmException;

import static com.project.bridge.entity.QRoleEntity.roleEntity;

@Repository
@Slf4j
public class RoleRepositorySupport extends BridgeQueryDslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    private RoleRepository roleRepository;

    public RoleRepositorySupport(@Qualifier("bridgeJpaQueryFactory") JPAQueryFactory jpaQueryFactory) {
        super(RoleEntity.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    //회원가입 시 권한 저장
    public RoleEntity saveRole(Long userIdx) throws NoSuchAlgorithmException {
        return roleRepository.save(RoleEntity.builder()
                .userIdx(userIdx)
                .role("USER")
                .build());
    }

    //권한 확인
    public String findRoleByUserId(Long userIdx){
        return jpaQueryFactory.select(roleEntity.role)
                .from(roleEntity)
                .where(roleEntity.userIdx.eq(userIdx))
                .fetchFirst();
    }

}
