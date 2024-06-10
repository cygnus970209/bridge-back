package com.project.bridge.repositories;

import com.project.bridge.entity.MailAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<MailAuthEntity,Long> {
}
