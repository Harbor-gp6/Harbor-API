package gp6.harbor.harborapi.dto.pedido.dto;

import gp6.harbor.harborapi.dto.prestador.dto.PrestadorListagemDto;
import gp6.harbor.harborapi.dto.produto.dto.ProdutoListagemDto;
import gp6.harbor.harborapi.dto.servico.dto.ServicoListagemDto;
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