package gp6.harbor.harborapi.service.email;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

    @Service
    @RequiredArgsConstructor
    public class EmailService {

        private final JavaMailSender mailSender;

        @Async
        public void sendEmail(String to, String subject, String text) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("harborApi@outlook.com");

            mailSender.send(message);
        }
    }