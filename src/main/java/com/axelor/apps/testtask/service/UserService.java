package com.axelor.apps.testtask.service;

import com.axelor.apps.testtask.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User findByEmail(String username);
    Boolean checkIfUserExists(String email);
    void save(User user);
}
