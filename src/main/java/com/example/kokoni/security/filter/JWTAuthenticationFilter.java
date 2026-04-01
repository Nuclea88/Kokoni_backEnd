package com.example.kokoni.security.filter;

import java.io.IOException;
import java.util.Date;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.kokoni.dto.request.LoginRequest;
import com.example.kokoni.dto.response.AuthResponse;
import com.example.kokoni.entity.User;
import com.example.kokoni.security.CustomAuthenticationManager;
import com.example.kokoni.security.UserDetail;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private CustomAuthenticationManager customAuthenticationManager;
    private final String secret;

    public JWTAuthenticationFilter(CustomAuthenticationManager customAuthenticationManager, String secret) {
        this.customAuthenticationManager = customAuthenticationManager;
        this.secret = secret;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest credentials = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(credentials.usernameOrEmail(), credentials.password());
            return customAuthenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el JSON de login", e);
        }
    }

    @Override
    public void unsuccessfulAuthentication (HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Credenciales inválidas");
        response.getWriter().flush();
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserDetail userDetail = (UserDetail) authResult.getPrincipal();
        User user = userDetail.getUser();

        String token = JWT.create()
                .withSubject(authResult.getName())
                .withClaim("userId", user.getId())
                .withExpiresAt(new Date(System.currentTimeMillis() + (480 * 60000)))
                .sign(Algorithm.HMAC512(this.secret));

            AuthResponse finalResponse = new AuthResponse(token);

        response.addHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        new ObjectMapper().writeValue(response.getWriter(), finalResponse);
    }
}

