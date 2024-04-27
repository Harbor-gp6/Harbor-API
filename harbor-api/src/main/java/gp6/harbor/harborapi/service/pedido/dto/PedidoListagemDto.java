package gp6.harbor.harborapi.service.pedido.dto;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.pedido.PedidoProduto;
import gp6.harbor.harborapi.domain.pedido.PedidoServico;
import gp6.harbor.harborapi.service.cliente.dto.ClienteCriacaoDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PedidoListagemDto {
    //private Integer id;

    private String cliente;

    private List<PedidoProduto> listaProduto;

    private List<PedidoServico> listaServico;

    private LocalDateTime dataAgendamento;

    private String observacao;
}