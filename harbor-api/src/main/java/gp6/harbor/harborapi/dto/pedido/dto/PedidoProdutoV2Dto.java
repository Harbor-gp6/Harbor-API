package gp6.harbor.harborapi.dto.pedido.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoProdutoV2Dto {
    @NotNull
    private Integer id;
    @NotNull
    private Integer quantidade;

}
