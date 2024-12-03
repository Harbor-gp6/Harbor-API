package gp6.harbor.harborapi.api.controller.Email;

import gp6.harbor.harborapi.service.email.EmailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/email")
    @RequiredArgsConstructor
    public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    @SecurityRequirement(name = "Bearer")
    public String sendEmail(@RequestParam String to,
                            @RequestParam String subject,
                            @RequestParam String body) {
        return emailService.sendEmail(to, subject, body);
    }
}
