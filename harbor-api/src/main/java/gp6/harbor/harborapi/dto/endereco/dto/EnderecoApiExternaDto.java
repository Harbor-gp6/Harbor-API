package gp6.harbor.harborapi.dto.endereco.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoApiExternaDto {

    private String cep;

    @JsonProperty(value = "uf")
    private String estado;

    @JsonProperty(value = "localidade")
    private String cidade;

    private String bairro;

    @JsonProperty(value = "logradouro")
    private String rua;

    @Override
    public String toString() {
        return "Endereco{" +
                "cep='" + cep + '\'' +
                ", estado='" + estado + '\'' +
                ", cidade='" + cidade + '\'' +
                ", bairro='" + bairro + '\'' +
                ", rua='" + rua + '\'' +
                '}';
    }
}
