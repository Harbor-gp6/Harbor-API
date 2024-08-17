package gp6.harbor.harborapi.dto.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioCriacaoDto {

    @Size(min = 3, max = 10)
    @Schema(description = "Nome do usuário", example = "Reis")
    private String nome;

    @Email
    @Schema(description = "Email do usuário", example = "rafael.reis@sptech.school")
    private String email;

    @Size(min = 6, max = 20)
    @Schema(description = "Senha do usuário", example = "123456")
    private String senha;
}
