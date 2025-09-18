package com.axelor.apps.testtask.service.impl;

import com.axelor.apps.testtask.model.Task;
import com.axelor.apps.testtask.service.MailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String MAIL_FROM;

    @Override
    public void sendEmail(String to, Task task) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setFrom(MAIL_FROM, "Тестовое задание");
            helper.setTo(to);
            helper.setSubject("Отправляю уведомление о только что созданной задаче");

            String body = "<h3>Задача</h3>"
                        + "<p><b>Название задачи</b> " + task.getTaskName() + "</p>"
                        + "<p><b>Описание:</b> " + task.getDescription() + "</p>";

            helper.setText(body, true);

            log.info("Отправка на почту {}", to);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
