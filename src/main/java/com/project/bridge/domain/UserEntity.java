package com.project.bridge.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;



@Getter
@Entity
@Table(name = "MEMBER")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Long userIdx;

    private String email;

    private String password;

    @Column(name = "user_name")
    private String userName;

    private String nickname;

    @Column(name = "created_dt")
    @CreationTimestamp
    private LocalDateTime createdDt;
    
    

}
