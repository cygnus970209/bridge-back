package com.project.bridge.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MEMBER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idx")
    private Long idx;

    @Column(name="user_email")
    private String userEmail;

    @Column(name ="nickname")
    private String nickname;

    @Column(name="password")
    private String password;

}
