package gp6.harbor.harborapi.service.endereco.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoDto {

    private String cep;
    private String estado;
    private String cidade;
    private String bairro;
    private String rua;

}
