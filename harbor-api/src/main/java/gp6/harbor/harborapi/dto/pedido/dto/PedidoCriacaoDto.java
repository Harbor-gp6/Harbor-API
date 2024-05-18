package gp6.harbor.harborapi.dto.pedido.dto;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PedidoCriacaoDto {
    private Cliente cliente;

    private List<Integer> listaProdutoIds;

    private List<Integer> listaServicoIds;

    private LocalDateTime dataAgendamento;

    private Integer prestadorId;

    private String observacao;
}