package com.axelor.apps.testtask.service.impl;

import com.axelor.apps.testtask.exception.UserNotFoundException;
import com.axelor.apps.testtask.model.User;
import com.axelor.apps.testtask.repository.UserRepository;
import com.axelor.apps.testtask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findByEmail(String username) {
        return userRepository
                .findUserByEmail(username)
                .orElseThrow(
                        () -> new UserNotFoundException("Пользователь с почтой " + username + " не найден")
                );
    }

    @Override
    public Boolean checkIfUserExists(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
