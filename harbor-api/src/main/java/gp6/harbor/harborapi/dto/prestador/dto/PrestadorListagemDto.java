package gp6.harbor.harborapi.dto.prestador.dto;

import gp6.harbor.harborapi.api.enums.CargoEnum;
import gp6.harbor.harborapi.dto.empresa.dto.EmpresaListagemDto;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class PrestadorListagemDto {
    private Integer id;
    private EmpresaListagemDto empresa;
    private byte[] foto;
    private String nome;
    private String sobrenome;
    private String telefone;
    private String cpf;
    private String email;
    private CargoEnum cargo;

}
