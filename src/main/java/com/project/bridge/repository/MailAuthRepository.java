package com.project.bridge.repository;

import com.project.bridge.entity.MailAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface MailAuthRepository extends JpaRepository<MailAuthEntity, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE MailAuthEntity SET deletedDt = :deletedDt WHERE email = :email")
    int updateFailStatusByEmail(@Param("email") String email, @Param("deletedDt") LocalDateTime deletedDt);

    @Modifying
    @Transactional
    @Query("UPDATE MailAuthEntity SET status = true WHERE email = :email AND deletedDt IS NULL")
    int updateStatusByEmail(@Param("email") String email);

}
