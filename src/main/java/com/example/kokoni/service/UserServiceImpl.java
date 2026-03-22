package com.example.kokoni.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.kokoni.dto.request.RegisterRequest;
import com.example.kokoni.dto.request.UpdateUserRequest;
import com.example.kokoni.dto.response.UserProfileResponse;
import com.example.kokoni.entity.User;
import com.example.kokoni.mapper.UserMapper;
import com.example.kokoni.repository.UserChapterProgressRepository;
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
    private final AuthService authService;
    private final UserChapterProgressRepository progressRepository;

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
        if (userRepository.existsByEmail(request.email())) {
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
        User user = authService.getAuthenticatedUser();
        Long userId = user.getId();
   
        int chaptersRead = (int) progressRepository.countByTrackerUserId(userId);
        int hoursSpent = (chaptersRead * 10) / 60;
        int level = (int) Math.floor(Math.sqrt(chaptersRead / 5.0));
        String rank = calculateRank(level);
        int streak = calculateStreak(userId);
        return userMapper.toProfileResponse(user, level, rank, chaptersRead, streak, hoursSpent);
    }

    private String calculateRank(int level) {
        if (level >= 46) return "KAGE";
        if (level >= 31) return "CLASE S";
        if (level >= 21) return "SHINOBI";
        if (level >= 11) return "NOVAT@";
        return "KAMI SAMA";
    }
    private int calculateStreak(Long userId) {
        List<LocalDate> readDates = progressRepository.findDistinctReadDatesByUserId(userId);
        if (readDates.isEmpty()) return 0;
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        
        if (!readDates.get(0).equals(today) && !readDates.get(0).equals(yesterday)) {
            return 0;
        }
        int streak = 0;
        LocalDate expectedDate = readDates.get(0);
        for (LocalDate date : readDates) {
            if (date.equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            } else {
                break;
            }
        }
        return streak;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

    @Override
    @Transactional
    public void updateUser(UpdateUserRequest request) {
        User existingUser = authService.getAuthenticatedUser();

        if (request.username() != null)
            existingUser.setUsername(request.username());
        if (request.email() != null)
            existingUser.setEmail(request.email());
        if (request.avatarUrl() != null)
            existingUser.setAvatarUrl(request.avatarUrl());

        if (request.password() != null && !request.password().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(request.password()));
        }
        userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser() {
        User user = authService.getAuthenticatedUser();
        userRepository.deleteById(user.getId());
    }
}
