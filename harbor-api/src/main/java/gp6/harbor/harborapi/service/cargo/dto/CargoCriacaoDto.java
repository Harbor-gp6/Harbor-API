package gp6.harbor.harborapi.service.cargo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CargoCriacaoDto {

    @NotBlank(message = "O nome do cargo não pode estar em branco")
    private String nomeCargo;
}
