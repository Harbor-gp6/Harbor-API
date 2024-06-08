package gp6.harbor.harborapi.service.email;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
    public class EmailService {

        private final JavaMailSender mailSender;

        public void sendEmail(String senderEmail, String toEmail, String subject, String body) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom(senderEmail);

            mailSender.send(message);
        }
    }
