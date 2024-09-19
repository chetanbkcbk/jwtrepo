package com.example.ums.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${myapp.jwt.secret}")
    private String secret;

    @Value("${myapp.jwt.access_expiry}")
    private long access_expiry;

    @Value("${myapp.jwt.refresh_expiry}")
    private long refresh_expiry;

     String createJwt(String userId, String email, String role,long expiry){
        return Jwts.builder()
                .setClaims(Map.of("userId", userId, "email", email, "role", role))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(getSignInKey())
                .compact();
    }

    private Key getSignInKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public Claims parseJwt(String token){
        JwtParser jwtParser=Jwts.parserBuilder().setSigningKey(getSignInKey()).build();
        return jwtParser.parseClaimsJws(token).getBody();

    }

    public String generateAccessToken(String userId,String email,String role){
              return  createJwt(userId,email,role,access_expiry);
    }

    public String generateRefreshToken(String userId,String email,String role){
        return createJwt(userId,email,role,refresh_expiry);
    }

}
