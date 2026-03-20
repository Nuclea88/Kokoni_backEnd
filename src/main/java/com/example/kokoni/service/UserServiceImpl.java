package com.example.kokoni.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.kokoni.dto.request.RegisterRequest;
import com.example.kokoni.dto.request.UpdateUserRequest;
import com.example.kokoni.dto.response.UserProfileResponse;
import com.example.kokoni.entity.User;
import com.example.kokoni.mapper.UserMapper;
import com.example.kokoni.repository.UserRepository;
import com.example.kokoni.security.UserDetail;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CustomListService customListService;
    private final PasswordEncoder passwordEncoder; 
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(usernameOrEmail)
                .orElseGet(() -> userRepository.findByEmail(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado")));
                
        return new UserDetail(user);
    }

    @Override
    @Transactional
    public User registerUser(RegisterRequest request) {
        
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }
        if (userRepository.existsByEmail(request.username())) {
            throw new RuntimeException("El email ya está registrado");
        }

        User newUser = userMapper.toEntity(request);
        newUser.setPassword(passwordEncoder.encode(request.password()));
        User savedUser = userRepository.save(newUser);
        customListService.createDefaultList(savedUser, "Favoritos");
        return savedUser;
    }

     @Override
    public UserProfileResponse getUserProfile() {
        Long myId = getAuthenticatedUserId();
        User user = findById(myId);
        //  A futuro, hacer queries reales a la tabla UserChapterProgress para sumar estos números.
        // Por ahora hardcodeamos unos valores para que el Frontend ya pueda pintar la tarjeta "LV 42".
        Integer mockedLevel = 42;
        String mockedRank = "ARCHMAGE RANK";
        Integer mockedChaptersRead = 12482;
        Integer mockedStreak = 14;
        Integer mockedHours = 842;
        return userMapper.toProfileResponse(user, mockedLevel, mockedRank, mockedChaptersRead, mockedStreak, mockedHours);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

    @Override
    @Transactional
    public void updateUser(UpdateUserRequest request) {
        Long myId = getAuthenticatedUserId();
        User existingUser = userRepository.findById(myId)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        if (request.username() != null) existingUser.setUsername(request.username());
        if (request.email() != null) existingUser.setEmail(request.email());
        if (request.avatarUrl() != null) existingUser.setAvatarUrl(request.avatarUrl());
        
        if (request.password() != null && !request.password().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(request.password()));
        }
        userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser() {
    Long myId = getAuthenticatedUserId();
        userRepository.deleteById(myId);
    }


private Long getAuthenticatedUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
        throw new RuntimeException("No autorizado");
    }
    UserDetail userDetail = (UserDetail) authentication.getPrincipal();
    return userDetail.getUser().getId();
}
}

