package com.axelor.apps.testtask.service;

import com.axelor.apps.testtask.model.Task;
import org.springframework.stereotype.Service;

@Service
public interface MailService {
    void sendEmail(String to, Task task);
}
