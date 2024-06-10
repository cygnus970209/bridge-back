package com.project.bridge.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ROLE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idx")
    private Long idx;

    @Column(name = "user_idx")
    private Long userIdx;

    @Column(name="role")
    private String role;

}
