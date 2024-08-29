package gp6.harbor.harborapi.dto.prestador.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;

@Getter
@Setter
public class PrestadorEstrelasDto {
    @NotNull
    private Long idPrestador;
    @Min(0)
    @Max(5)
    @NotNull
    private Double estrelas;
    @NotNull
    @CNPJ
    private String cnpjEmpresa;
    private String comentario;
}
