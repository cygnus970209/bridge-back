package com.project.bridge.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MAIL_AUTH")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailAuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idx")
    private Long idx;

    @Column(name="code")
    private Integer code;

    @Column(name="email")
    private String email;
}
