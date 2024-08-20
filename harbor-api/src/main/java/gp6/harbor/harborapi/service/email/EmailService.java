package gp6.harbor.harborapi.service.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${email.sender}")
    private String fromEmail;

    public void sendEmail(String toEmail, String subject, String body) {
        validateEmailParameters(toEmail, subject, body);
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(fromEmail);

        try {
            mailSender.send(message);
        } catch (MailException e) {
            logger.error("Erro ao enviar e-mail: {}", e.getMessage());
        }
    }

    private void validateEmailParameters(String toEmail, String subject, String body) {
        if (toEmail == null || toEmail.isEmpty()) {
            throw new IllegalArgumentException("O endereço de e-mail não pode ser nulo ou vazio.");
        }
        if (subject == null || subject.isEmpty()) {
            throw new IllegalArgumentException("O assunto não pode ser nulo ou vazio.");
        }
        if (body == null || body.isEmpty()) {
            throw new IllegalArgumentException("O corpo do e-mail não pode ser nulo ou vazio.");
        }
    }
}
