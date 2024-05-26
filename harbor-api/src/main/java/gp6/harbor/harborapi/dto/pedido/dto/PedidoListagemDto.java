package gp6.harbor.harborapi.dto.pedido.dto;

import gp6.harbor.harborapi.api.enums.FormaPagamento;
import gp6.harbor.harborapi.domain.servico.Servico;
import gp6.harbor.harborapi.dto.prestador.dto.PrestadorListagemDto;
import gp6.harbor.harborapi.dto.produto.dto.ProdutoListagemDto;
import gp6.harbor.harborapi.dto.servico.dto.ServicoListagemDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PedidoListagemDto {
    private Integer id;

    private String cliente;

    private List<PedidoProdutoListagemDto> listaProduto;

    private List<PedidoServicoListagemDto> listaServico;

    private LocalDateTime dataAgendamento;

    private PrestadorListagemDto prestador;

    private FormaPagamento formaPagamento;

    @Data
    public static class PedidoServicoListagemDto {
        private Integer id;
        private Servico servico;
    }

    @Data
    public static class PedidoProdutoListagemDto {
        private Integer id;
        private ProdutoListagemDto produto;
        private Integer quantidade;
    }
}