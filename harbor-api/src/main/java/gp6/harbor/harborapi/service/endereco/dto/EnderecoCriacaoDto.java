package gp6.harbor.harborapi.service.endereco.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoCriacaoDto {

    private String bairro;
    @NotBlank(message = "O logradouro não pode estar em branco")
    private String logradouro;
    @NotBlank(message = "O cidade não pode estar em branco")
    private String cidade;
    @NotBlank(message = "O estado não pode estar em branco")
    private String estado;
    private String numero;
    @NotBlank(message = "O cep não pode estar em branco")
    @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 dígitos numéricos")
    private String cep;

    private String complemento;
}
