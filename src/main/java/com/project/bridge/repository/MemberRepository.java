package com.project.bridge.repository;

import com.project.bridge.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long>{
    MemberEntity findByNickname(String nickname);

    MemberEntity findByEmail(String email);

    MemberEntity findByIdx(Long idx);
}
