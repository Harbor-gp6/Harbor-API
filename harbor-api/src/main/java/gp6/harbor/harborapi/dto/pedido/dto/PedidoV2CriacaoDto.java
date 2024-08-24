package gp6.harbor.harborapi.dto.pedido.dto;

import gp6.harbor.harborapi.api.enums.FormaPagamentoEnum;
import gp6.harbor.harborapi.domain.pedido.PedidoPrestador;
import gp6.harbor.harborapi.domain.pedido.PedidoProdutoV2;
import gp6.harbor.harborapi.dto.cliente.dto.ClienteCriacaoDto;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PedidoV2CriacaoDto {
    @NotNull
    private ClienteCriacaoDto cliente;

    @NotNull
    private List<PedidoPrestador> pedidoPrestador;

    private List<PedidoProdutoV2> pedidoProdutos;

    @CNPJ
    @CNPJ
    private String cnpjEmpresa;

    @NotNull
    private Enum<FormaPagamentoEnum> formaPagamento;
}
