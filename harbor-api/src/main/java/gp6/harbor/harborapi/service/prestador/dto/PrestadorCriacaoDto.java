package gp6.harbor.harborapi.service.prestador.dto;


import gp6.harbor.harborapi.domain.cargo.Cargo;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.service.cargo.dto.CargoCriacaoDto;
import gp6.harbor.harborapi.service.empresa.dto.EmpresaCriacaoDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Setter
@Getter
public class PrestadorCriacaoDto {
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
    @NotBlank(message = "O cpf não pode estar em branco")
    private String cpf;
    @Email
    @NotBlank(message = "O email não pode estar em branco")
    private String email;
    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 8, max = 30)
    private String senha;
    @Valid
    private Cargo cargo;
    @Valid
    private Empresa empresa;

}
