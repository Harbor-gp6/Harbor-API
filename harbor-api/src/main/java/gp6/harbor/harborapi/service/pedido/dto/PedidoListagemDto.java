package gp6.harbor.harborapi.service.pedido.dto;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.pedido.PedidoProduto;
import gp6.harbor.harborapi.domain.pedido.PedidoServico;
import gp6.harbor.harborapi.domain.produto.Produto;
import gp6.harbor.harborapi.domain.servico.Servico;
import gp6.harbor.harborapi.service.cliente.dto.ClienteCriacaoDto;
import gp6.harbor.harborapi.service.prestador.dto.PrestadorListagemDto;
import gp6.harbor.harborapi.service.produto.dto.ProdutoListagemDto;
import gp6.harbor.harborapi.service.servico.dto.ServicoListagemDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PedidoListagemDto {
    //private Integer id;

    private String cliente;

    private List<ProdutoListagemDto> listaProduto;

    private List<ServicoListagemDto> listaServico;

    private LocalDateTime dataAgendamento;

    private PrestadorListagemDto prestador;

    private String observacao;
}