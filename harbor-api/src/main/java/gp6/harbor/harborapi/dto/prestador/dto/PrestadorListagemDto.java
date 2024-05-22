package gp6.harbor.harborapi.dto.prestador.dto;

import gp6.harbor.harborapi.domain.cargo.Cargo;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class PrestadorListagemDto {
    private Integer id;
    private Empresa empresa;
    private byte[] foto;
    private String nome;
    private String sobrenome;
    private String telefone;
    private String cpf;
    private String email;
    private Cargo cargo;

}
