package gp6.harbor.harborapi.dto.pedido.dto;

import gp6.harbor.harborapi.api.enums.FormaPagamento;
import gp6.harbor.harborapi.domain.pedido.Pedido;
import gp6.harbor.harborapi.domain.pedido.PedidoProduto;
import gp6.harbor.harborapi.domain.pedido.PedidoServico;
import gp6.harbor.harborapi.domain.produto.Produto;
import gp6.harbor.harborapi.domain.produto.repository.ProdutoRepository;
import gp6.harbor.harborapi.domain.servico.Servico;
import gp6.harbor.harborapi.domain.servico.repository.ServicoRepository;
import gp6.harbor.harborapi.dto.cliente.dto.ClienteMapper;
import gp6.harbor.harborapi.dto.prestador.dto.PrestadorMapper;
import gp6.harbor.harborapi.dto.produto.dto.ProdutoMapper;
import gp6.harbor.harborapi.dto.servico.dto.ServicoMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class PedidoMapper {

    public static Pedido toEntity(PedidoCriacaoDto dto){
        if (dto == null){
            return null;
        }
        Pedido pedido = new Pedido();

        pedido.setCliente(ClienteMapper.toEntity(dto.getCliente()));

        pedido.setDataAgendamento(dto.getDataAgendamento());

        pedido.setFormaPagamento(FormaPagamento.fromCodigo(dto.getFormaPagamento()));

        return pedido;

    }

    public static PedidoListagemDto toDto(Pedido entity) {
        if (entity == null) {
            return null;
        }
        PedidoListagemDto pedidoListagemDto = new PedidoListagemDto();

        pedidoListagemDto.setId(entity.getId());

        pedidoListagemDto.setCliente(entity.getCliente().getNome() + " " + entity.getCliente().getSobrenome());

        pedidoListagemDto.setPrestador(PrestadorMapper.toDto(entity.getPrestador()));

        pedidoListagemDto.setDataAgendamento(entity.getDataAgendamento());

        if (entity.getPedidoProdutos() != null) {
            pedidoListagemDto.setListaProduto(toProdutoDto(entity.getPedidoProdutos()));
        }

        pedidoListagemDto.setListaServico(toServicoDto(entity.getPedidoServicos()));

        pedidoListagemDto.setFormaPagamento(entity.getFormaPagamento());

        return pedidoListagemDto;
    }

    public static List<PedidoListagemDto> toDto(List<Pedido> entities) {
        return entities.stream().map(PedidoMapper::toDto).toList();
    }

    private static PedidoListagemDto.PedidoProdutoListagemDto toProdutoDto(PedidoProduto entity) {
        PedidoListagemDto.PedidoProdutoListagemDto dto = new PedidoListagemDto.PedidoProdutoListagemDto();

        dto.setId(entity.getId());
        dto.setProduto(dto.getProduto());
        dto.setQuantidade(entity.getQuantidade());

        return dto;
    }

    private static List<PedidoListagemDto.PedidoProdutoListagemDto> toProdutoDto(List<PedidoProduto> entities) {
        return entities.stream().map(PedidoMapper::toProdutoDto).toList();
    }

    private static PedidoListagemDto.PedidoServicoListagemDto toServicoDto(PedidoServico entity) {
        PedidoListagemDto.PedidoServicoListagemDto dto = new PedidoListagemDto.PedidoServicoListagemDto();

        dto.setId(entity.getId());
        dto.setServico(entity.getServico());

        return dto;
    }

    private static List<PedidoListagemDto.PedidoServicoListagemDto> toServicoDto(List<PedidoServico> entities) {
        return entities.stream().map(PedidoMapper::toServicoDto).toList();
    }
}

