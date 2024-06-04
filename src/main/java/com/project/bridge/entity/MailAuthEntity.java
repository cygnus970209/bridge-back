package com.project.bridge.entity;

import jakarta.persistence.*;
import lombok.*;

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



    @Column(name = "created_dt")
    private LocalDateTime createdDt;
}
