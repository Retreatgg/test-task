package com.axelor.apps.testtask.service.impl;

import com.axelor.apps.testtask.dto.RegisterRequest;
import com.axelor.apps.testtask.model.User;
import com.axelor.apps.testtask.service.AuthService;
import com.axelor.apps.testtask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {
        Boolean isExists = userService.checkIfUserExists(request.getEmail());
        if (isExists) {
            throw new IllegalArgumentException("Пользователь с такой почтой уже существует");
        }

        User user = mapToUser(request);
        userService.save(user);
    }

    private User mapToUser(RegisterRequest request) {
        return User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .surname(request.getSurname())
                .enabled(true)
                .build();
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userService.findByEmail(userDetails.getUsername());
        }
        return null;
    }
}
