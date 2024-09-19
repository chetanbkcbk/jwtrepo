package com.example.ums.entity;

import com.example.ums.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int userId;
    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
