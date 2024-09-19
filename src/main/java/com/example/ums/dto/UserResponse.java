package com.example.ums.dto;

import com.example.ums.enums.UserRole;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private int userId;
    private String username;
    private String email;
    private UserRole role;
}
