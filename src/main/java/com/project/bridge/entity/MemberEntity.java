package com.project.bridge.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "MEMBER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "last_login_dt")
    private LocalDateTime lastLoginDt;

    @Column(name = "created_dt")
    private LocalDateTime createdDt;

    @Column(name = "updated_dt")
    private LocalDateTime updatedDt;

    @Column(name = "deleted_dt")
    private LocalDateTime deletedDt;
}
