package gp6.harbor.harborapi.dto.prestador.dto;


import gp6.harbor.harborapi.api.enums.CargoEnum;
import gp6.harbor.harborapi.dto.empresa.dto.EmpresaCriacaoDto;
import gp6.harbor.harborapi.dto.validations.OnlyOne;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Setter
@Getter
@OnlyOne(first = "empresa", second = "empresaId", message = "Apenas um entre empresa e empresaId deve ser preenchido")
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
    private EmpresaCriacaoDto empresa;
    private Integer empresaId;
    private String foto;
}
