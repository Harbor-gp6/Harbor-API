package gp6.harbor.harborapi.dto.pedido.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoPrestadorDto {
    @NotNull
    private Long prestadorId; // Certifique-se que o tipo está correto
    @NotNull
    private Integer servicoId;
}
