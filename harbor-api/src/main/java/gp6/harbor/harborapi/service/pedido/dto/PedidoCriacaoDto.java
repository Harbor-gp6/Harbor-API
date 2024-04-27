package gp6.harbor.harborapi.service.pedido.dto;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.pedido.PedidoProduto;
import gp6.harbor.harborapi.domain.pedido.PedidoServico;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.produto.Produto;
import gp6.harbor.harborapi.domain.servico.Servico;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    private String observacao;
}