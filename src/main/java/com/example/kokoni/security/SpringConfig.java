package com.example.kokoni.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.kokoni.security.filter.JWTAuthenticationFilter;
import com.example.kokoni.security.filter.JWTAuthorizationFilter;


@Configuration
public class SpringConfig {

    private CustomAuthenticationManager customAuthenticationManager;

    public SpringConfig(CustomAuthenticationManager customAuthenticationManager){
        this.customAuthenticationManager = customAuthenticationManager;
    }

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        JWTAuthenticationFilter authenticationFilter = new JWTAuthenticationFilter(customAuthenticationManager, jwtSecret);
        authenticationFilter.setFilterProcessesUrl("/login");

        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) 
            .csrf(csfr -> csfr.disable())
            .authorizeHttpRequests(request -> request
                .requestMatchers("/login", "/registrer").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/mangas/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilter(authenticationFilter)
            .addFilterAfter(new JWTAuthorizationFilter(jwtSecret), JWTAuthenticationFilter.class)
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            return http.build();
    }

@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); 
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
    configuration.setExposedHeaders(Arrays.asList("Authorization")); 
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
}