package gp6.harbor.harborapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import gp6.harbor.harborapi.service.email.EmailService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve Enviar Email")
    void sendEmail() {
        // Dados de teste
        String toEmail = "test@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        // Chama o método que será testado
        emailService.sendEmail(toEmail, subject, body);

        // Captura o argumento enviado para o método send
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());
        SimpleMailMessage capturedMessage = messageCaptor.getValue();

        // Verifica se os dados do email estão corretos
        assertEquals(subject, capturedMessage.getSubject());
        assertEquals(body, capturedMessage.getText());
        assertEquals("leonardo.souza@sptech.school", capturedMessage.getFrom());
    }
}
