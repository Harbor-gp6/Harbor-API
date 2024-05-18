package gp6.harbor.harborapi.dto.servico.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServicoCriacaoDto {
    @NotBlank(message = "A descrição do serviço não pode estar em branco")
    @Size(min = 2, max = 45, message = "A descrição do serviço deve ter entre 2 e 45 caracteres")
    private String descricaoServico;
    @NotNull
    private Boolean servicoEspecial;
    @NotNull
    @Min(value = 15)
    private Integer tempoMedioEmMinutos;
    @NotNull
    private Double valorServico;
    @NotNull
    private String CNPJempresa;
}
