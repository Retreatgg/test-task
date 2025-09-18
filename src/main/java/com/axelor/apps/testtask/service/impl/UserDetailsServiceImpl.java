package com.axelor.apps.testtask.service.impl;

import com.axelor.apps.testtask.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Transactional
    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = userService.findByEmail(username);
        return new User(
                user.getEmail(),
                user.getPassword(),
                user.getEnabled(),
                true,
                true,
                true,
                Collections.emptyList()
        );
    }
}
