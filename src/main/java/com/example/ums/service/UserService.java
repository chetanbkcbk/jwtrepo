package com.example.ums.service;

import com.example.ums.dto.LoginRequest;
import com.example.ums.dto.UserRequest;
import com.example.ums.dto.UserResponse;
import com.example.ums.entity.User;
import com.example.ums.enums.UserRole;
import com.example.ums.mapper.UserMapper;
import com.example.ums.repository.UserRepo;
import com.example.ums.security.JwtService;
import com.example.ums.util.ResponseStructure;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${myapp.jwt.access_expiry}")
    private long access_expiry;
    @Value("${myapp.jwt.refresh_expiry}")
    private long refresh_expiry;

    public UserService(UserMapper userMapper, UserRepo userRepo, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userMapper = userMapper;
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public UserResponse registerUser(UserRequest userRequest, UserRole role) {
        User user = userMapper.mapToUser(userRequest, new User());
        user.setRole(role);
        user = userRepo.save(user);
        return userMapper.mapToUserResponse(user);
    }

    public ResponseEntity<ResponseStructure<UserResponse>> login(LoginRequest loginRequest) {
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                        loginRequest.getPassword()));
        if(auth.isAuthenticated()){

            return userRepo.findByEmail(loginRequest.getEmail()).map(user -> {
                    HttpHeaders httpHeaders= grantAccess(user);
                       return ResponseEntity.ok().headers(httpHeaders).
                               body(ResponseStructure.generate(HttpStatus.OK.value(),"COOKIES IS CREATED",userMapper.mapToUserResponse(user)));
                    }) .orElseThrow(()->new NoSuchElementException("no element found!!"));
        } else
            throw new UsernameNotFoundException("no such username found");
    }
    private HttpHeaders grantAccess(User user){
        String accessToken = jwtService.generateAccessToken(String.valueOf(user.getUserId()), user.getEmail(), user.getRole().name());
        String refreshToken=jwtService.generateRefreshToken(String.valueOf(user.getUserId()),user.getEmail(),user.getRole().name());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, createCookie("at",accessToken,access_expiry));
        httpHeaders.add(HttpHeaders.SET_COOKIE, createCookie("rt",refreshToken,refresh_expiry));

        return httpHeaders;
    }

    private String createCookie(String name,String value,long maxAge){
        String cookiestring = ResponseCookie.from(name, value)
                .domain("localhost")
                .path("/")
                .secure(false)
                .httpOnly(true)
                .sameSite("Lax")
                .maxAge(maxAge).build().toString();
        return cookiestring;
    }
}




