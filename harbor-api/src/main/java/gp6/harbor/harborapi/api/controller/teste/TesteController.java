package gp6.harbor.harborapi.api.controller.teste;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class TesteController {
    @GetMapping
    public ResponseEntity<String> health() {
        return ResponseEntity.status(200).body("OK");
    }
}
