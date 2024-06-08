package gp6.harbor.harborapi.dto.cliente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Getter
@Setter
public class ClienteCriacaoDto {

    @NotBlank(message = "O nome não pode estar em branco")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;
    @NotBlank(message = "O sobrenome não pode estar em branco")
    @Size(min = 2, max = 100, message = "O sobrenome deve ter entre 2 e 100 caracteres")
    private String sobrenome;
    @NotBlank
    @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter apenas números e deve ter entre 10 ou 11 dígitos")
    private String telefone;
    @CPF
    @NotBlank
    private String cpf;
    @Email
    private String email;
}
