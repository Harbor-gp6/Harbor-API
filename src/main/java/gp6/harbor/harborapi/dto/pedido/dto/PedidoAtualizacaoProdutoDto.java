package gp6.harbor.harborapi.dto.pedido.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PedidoAtualizacaoProdutoDto {
    @NotNull
    private List<ProdutoDto> produtos;

    @Data
    public static class ProdutoDto {
        @NotNull
        private Integer id;
        @NotNull
        private Integer quantidade;
    }
}
