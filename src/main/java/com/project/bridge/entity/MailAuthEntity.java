package com.project.bridge.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "MAIL_AUTH")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailAuthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "email")
    private String email;

    @Column(name = "auth_no")
    private String authNo;

    @Column(name = "expired_dt")
    private LocalDateTime expiredDt;

    @Column(name = "created_dt")
    @CreationTimestamp
    private LocalDateTime createdDt;

    @Column(name = "updated_dt")
    private LocalDateTime updatedDt;

    @Column(name = "deleted_dt")
    private LocalDateTime deletedDt;
}
