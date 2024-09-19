package com.example.ums.controller;

import com.example.ums.dto.LoginRequest;
import com.example.ums.dto.UserRequest;
import com.example.ums.dto.UserResponse;
import com.example.ums.enums.UserRole;
import com.example.ums.service.UserService;
import com.example.ums.util.AppResponseBuilder;
import com.example.ums.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor

public class UserController {

    private final UserService userService;
    private final AppResponseBuilder responseBuilder;

    @PostMapping("/register")
    public ResponseEntity<ResponseStructure<UserResponse>> registerUser(@RequestBody UserRequest userRequest,
                                                                        @RequestParam(name = "role") UserRole role){
        UserResponse response = userService.registerUser(userRequest, role);
        return responseBuilder.build(HttpStatus.CREATED, "user created", response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseStructure<UserResponse>> login(@RequestBody LoginRequest loginRequest){
        return userService.login(loginRequest);
    }
}
