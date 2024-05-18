package gp6.harbor.harborapi.dto.empresa.dto;

import gp6.harbor.harborapi.domain.endereco.Endereco;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaListagemDto {

    private String razaoSocial;

    private String nomeFantasia;

    private String cnpj;

    private Endereco endereco;


}
