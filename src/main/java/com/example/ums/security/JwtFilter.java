package com.example.ums.security;

import com.example.ums.enums.UserRole;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("In Jwt Filter, Extracting token...");
        String token = request.getHeader("Authorization");
        if (token != null) {
            log.info("Token Found.");
            token = token.substring(7);
            if (!token.isEmpty()) {
                log.info("Token is empty. Extracting user name and email...");
                Claims claims = jwtService.parseJwt(token);
                String role = claims.get("role", String.class);
                String email = claims.get("email", String.class);

                if (role != null && email != null) {
                    log.info("User name and email found, setting up the security context.");
                    UserRole.valueOf(role);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority(role)));
                                     usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    log.info("Authentication successful");
                }
            }
        }

        filterChain.doFilter(request,response);

    }
}
