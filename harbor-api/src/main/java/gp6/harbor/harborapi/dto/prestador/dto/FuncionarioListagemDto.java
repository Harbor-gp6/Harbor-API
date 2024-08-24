package gp6.harbor.harborapi.dto.prestador.dto;

import gp6.harbor.harborapi.api.enums.CargoEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuncionarioListagemDto {
    private String nome;
    private String sobrenome;
    private String telefone;
    private String cpf;
    private String email;
    private CargoEnum cargo;

}
