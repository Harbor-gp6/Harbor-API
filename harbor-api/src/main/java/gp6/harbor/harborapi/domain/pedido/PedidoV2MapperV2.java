package gp6.harbor.harborapi.domain.pedido;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoV2MapperV2 {

    @Mapping(source = "id", target = "idPedido")
    @Mapping(source = "cliente", target = "nomeCliente", qualifiedByName = "mapNomeCompleto")
    @Mapping(source = "cliente.telefone", target = "telefoneCliente")
    @Mapping(source = "cliente.id", target = "idCliente")
    @Mapping(source = "cliente.cpf", target = "cpfCliente")
    @Mapping(source = "cliente.email", target = "emailCliente")
    @Mapping(source = "statusPedidoEnum.status", target = "statusPedidoEnum")
    @Mapping(source = "formaPagamentoEnum.formaPagamento", target = "formaPagamentoEnum")
    PedidoV2DTO toDto(PedidoV2 pedidoV2);

    List<PedidoV2DTO> toDtoList(List<PedidoV2> pedidos);

    @Mapping(source = "prestador.nome", target = "nomePrestador")
    @Mapping(source = "servico.descricaoServico", target = "descricaoServico")
    @Mapping(source = "servico.valorServico", target = "valorServico")
    PedidoPrestadorDTOV2 toDto(PedidoPrestador pedidoPrestador);

    List<PedidoPrestadorDTOV2> toDtoListPrestador(List<PedidoPrestador> pedidoPrestador);

    @Mapping(source = "produto.nome", target = "nomeProduto")
    @Mapping(source = "produto.precoVenda", target = "valorProduto")
    PedidoProdutoDTOV2 toDto(PedidoProdutoV2 pedidoProduto);

    List<PedidoProdutoDTOV2> toDtoListProdutos(List<PedidoProdutoV2> pedidoProdutos);

    @Named("mapNomeCompleto")
    default String mapNomeCompleto(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        return cliente.getNome() + " " + cliente.getSobrenome();
    }
}