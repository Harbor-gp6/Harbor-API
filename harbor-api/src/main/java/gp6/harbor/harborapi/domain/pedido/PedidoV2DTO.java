package gp6.harbor.harborapi.domain.pedido;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PedidoV2DTO {
    private Integer idPedido;
    private String nomeCliente;
    private String telefoneCliente;
    private List<PedidoPrestadorDTOV2> pedidoPrestador;
    private List<PedidoProdutoDTOV2> pedidoProdutos;
    private LocalDateTime dataCriacao;
    private String statusPedidoEnum;
    private LocalDateTime dataAgendamento;
    private String formaPagamentoEnum;
    private UUID codigoPedido;
    private Double totalPedido;
}