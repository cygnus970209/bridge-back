package com.project.bridge.repository;

import com.project.bridge.domain.MailAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailAuthRepository extends JpaRepository<MailAuthEntity, Long> {
    
    MailAuthEntity findByIdx(Long idx);
    
}
