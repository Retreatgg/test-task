package com.axelor.apps.testtask.service;

import com.axelor.apps.testtask.dto.RegisterRequest;
import com.axelor.apps.testtask.model.User;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    void register(RegisterRequest request);

    User getCurrentUser();
}
