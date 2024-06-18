package com.project.bridge.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MAIL_AUTH")
public class MailAuthEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    
    @Column(name = "auth_no")
    private String authNo;
    
    private String email;
    
    @Column(name = "created_dt")
    @CreationTimestamp
    private LocalDateTime createdDt;
    
    @Column(name = "is_auth")
    private int isAuth;
    
    @Column(name = "auth_dt")
    private LocalDateTime authDt;
}
