package gp6.harbor.harborapi.domain.pedido;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoProdutoDTOV2 {
    private Integer idProduto;
    private String nomeProduto;
    private Integer quantidade;
    private Double valorProduto;
}
