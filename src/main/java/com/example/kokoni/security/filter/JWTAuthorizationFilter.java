// package com.example.kokoni.security.filter;

// import java.io.IOException;
// import java.util.Collections;
// import java.util.List;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.web.filter.OncePerRequestFilter;
// import com.auth0.jwt.JWT;
// import com.auth0.jwt.algorithms.Algorithm;
// import com.auth0.jwt.interfaces.DecodedJWT;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// public class JWTAuthorizationFilter extends OncePerRequestFilter {
    
//     private final String secret;

//     public JWTAuthorizationFilter(String secret){
//         this.secret = secret;
//     }

//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//         String header = request.getHeader("Authorization");

//         if (header == null || !header.startsWith("Bearer ")) {
//             filterChain.doFilter(request, response);
//             return;
//         }
//         try{
//         String token = header.replace("Bearer ", "");

//         DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(secret))
//             .build()
//             .verify(token);

//         String username = decodedJWT.getSubject();
//         int userId = decodedJWT.getClaim("userId").asInt();

//         List<GrantedAuthority> authorities = Collections.emptyList();
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
//         authentication.setDetails(userId);

//         SecurityContextHolder.getContext().setAuthentication(authentication);

//         }catch (Exception e){
//             SecurityContextHolder.clearContext();
//         }

//         filterChain.doFilter(request, response);
//     }

// }