package com.example.kokoni.service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.kokoni.entity.User;
import com.example.kokoni.repository.UserRepository;
import com.example.kokoni.security.UserDetail;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

private final UserRepository userRepository;
    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("No autorizado: Debes estar logueado.");
        }
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        return userRepository.findById(userDetail.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado en la base de datos"));
    }

    @Override
    public User getOptionalAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && 
            authentication.isAuthenticated() && 
            authentication.getPrincipal() instanceof UserDetail) {
            
            UserDetail userDetail = (UserDetail) authentication.getPrincipal();
            return userRepository.findById(userDetail.getUser().getId()).orElse(null);
        }
        return null;
    }
}