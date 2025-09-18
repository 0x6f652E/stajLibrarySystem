package com.ibb.library.entity;

import com.ibb.library.security.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users") // "user" reserved kelime olmasın diye "users"
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User extends BaseEntity {

    @Column(nullable = false, length = 80)
    private String firstName;

    @Column(nullable = false, length = 80)
    private String lastName;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;
}
