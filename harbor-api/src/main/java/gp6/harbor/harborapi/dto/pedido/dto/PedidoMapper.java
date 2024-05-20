package gp6.harbor.harborapi.dto.pedido.dto;

import gp6.harbor.harborapi.domain.pedido.Pedido;
import gp6.harbor.harborapi.domain.produto.Produto;
import gp6.harbor.harborapi.domain.produto.repository.ProdutoRepository;
import gp6.harbor.harborapi.domain.servico.Servico;
import gp6.harbor.harborapi.domain.servico.repository.ServicoRepository;
import gp6.harbor.harborapi.dto.prestador.dto.PrestadorMapper;
import gp6.harbor.harborapi.dto.produto.dto.ProdutoMapper;
import gp6.harbor.harborapi.dto.servico.dto.ServicoMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class PedidoMapper {

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public static Pedido toEntity(PedidoCriacaoDto dto){
        if (dto == null){
            return null;
        }
        Pedido pedido = new Pedido();
        Produto produto = new Produto();
        Servico servico = new Servico();

        pedido.setCliente(dto.getCliente());

        pedido.setDataAgendamento(dto.getDataAgendamento());
        pedido.setObservacao(dto.getObservacao());

        return pedido;

    }

    public static PedidoListagemDto toDto(Pedido entity) {
        if (entity == null) {
            return null;
        }
        PedidoListagemDto pedidoListagemDto = new PedidoListagemDto();

        pedidoListagemDto.setCliente(entity.getCliente().getNome() + " " + entity.getCliente().getSobrenome());

        pedidoListagemDto.setPrestador(PrestadorMapper.toDto(entity.getPrestador()));

        pedidoListagemDto.setDataAgendamento(entity.getDataAgendamento());
        pedidoListagemDto.setObservacao(entity.getObservacao());

        return pedidoListagemDto;
    }

    public static List<PedidoListagemDto> toDto(List<Pedido> entities) {
        return entities.stream().map(PedidoMapper::toDto).toList();
    }
}

