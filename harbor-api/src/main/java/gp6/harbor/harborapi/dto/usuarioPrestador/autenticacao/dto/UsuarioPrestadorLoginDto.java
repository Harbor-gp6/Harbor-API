package gp6.harbor.harborapi.dto.usuarioPrestador.autenticacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioPrestadorLoginDto {

    @Schema(description = "E-mail do usuário", example = "john@doe.com")
    private String email;
    @Schema(description = "Senha do usuário", example = "123456")
    private String senha;

}
