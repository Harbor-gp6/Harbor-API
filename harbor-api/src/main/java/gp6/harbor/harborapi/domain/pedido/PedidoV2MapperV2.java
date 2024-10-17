package gp6.harbor.harborapi.domain.pedido;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoV2MapperV2 {

    @Mapping(source = "id", target = "idPedido")
    @Mapping(source = "cliente.nome", target = "nomeCliente")
    @Mapping(source = "cliente.telefone", target = "telefoneCliente")
    @Mapping(source = "statusPedidoEnum.status", target = "statusPedidoEnum")
    @Mapping(source = "formaPagamentoEnum.formaPagamento", target = "formaPagamentoEnum")
    PedidoV2DTO toDto(PedidoV2 pedidoV2);

    List<PedidoV2DTO> toDtoList(List<PedidoV2> pedidos);

    @Mapping(source = "prestador.nome", target = "nomePrestador")
    @Mapping(source = "servico.descricaoServico", target = "descricaoServico") // Use descricaoServico corretamente
    PedidoPrestadorDTOV2 toDto(PedidoPrestador pedidoPrestador);

    List<PedidoPrestadorDTOV2> toDtoListPrestador(List<PedidoPrestador> pedidoPrestador);

    @Mapping(source = "produto.nome", target = "nomeProduto")
    PedidoProdutoDTOV2 toDto(PedidoProdutoV2 pedidoProduto);

    List<PedidoProdutoDTOV2> toDtoListProdutos(List<PedidoProdutoV2> pedidoProdutos);
}