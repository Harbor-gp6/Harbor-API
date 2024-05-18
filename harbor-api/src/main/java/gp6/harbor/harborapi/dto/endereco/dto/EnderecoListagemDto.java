package gp6.harbor.harborapi.dto.endereco.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoListagemDto {
    private Integer id;
    private String logradouro;
    private String cidade;
    private String estado;
    private String numero;
    private String cep;
    private String complemento;
}
