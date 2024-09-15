package gp6.harbor.harborapi.dto.pedido.dto;

import gp6.harbor.harborapi.domain.pedido.PedidoPrestador;
import gp6.harbor.harborapi.domain.pedido.PedidoProdutoV2;
import gp6.harbor.harborapi.domain.pedido.PedidoV2;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoV2Mapper {
    @Mapping(source = "cliente.id", target = "idcliente")
    @Mapping(source = "empresa.cnpj", target = "cnpjEmpresa")
    @Mapping(source = "pedidoPrestador", target = "pedidoPrestador") // Mapeia usando PedidoPrestadorMapper
    @Mapping(source = "pedidoProdutos", target = "pedidoProdutos")   // Mapeia usando PedidoProdutoV2Mapper
    PedidoV2ListagemDto toDto(PedidoV2 pedido);

    List<PedidoV2ListagemDto> toDto(List<PedidoV2> pedidoList);

    // Mapeia de PedidoV2CriacaoDto para PedidoV2
    @Mapping(target = "id", ignore = true) // Ignora o campo id
    PedidoV2 toEntity(PedidoV2CriacaoDto pedidoDto);

    // Mapeia de PedidoPrestador para PedidoPrestadorDto

    PedidoPrestadorDto toPedidoPrestadorDto(PedidoPrestador pedidoPrestador);

    // Mapeia de PedidoPrestadorDto para PedidoPrestador
    @Mapping(target = "id", ignore = true) // Ignora o campo id
    PedidoPrestador toPedidoPrestadorEntity(PedidoPrestadorDto pedidoPrestadorDto);

    // Mapeia de PedidoProdutoV2 para PedidoProdutoV2Dto
    PedidoProdutoV2Dto toPedidoProdutoV2Dto(PedidoProdutoV2 pedidoProduto);

    // Mapeia de PedidoProdutoV2Dto para PedidoProdutoV2
    @Mapping(target = "id", ignore = true) // Ignora o campo id
    PedidoProdutoV2 toPedidoProdutoV2Entity(PedidoProdutoV2Dto pedidoProdutoDto);

    // Mapeia listas de PedidoPrestador
    List<PedidoPrestadorDto> toPedidoPrestadorDtoList(List<PedidoPrestador> pedidoPrestadorList);
    List<PedidoPrestador> toPedidoPrestadorEntityList(List<PedidoPrestadorDto> pedidoPrestadorDtoList);

    // Mapeia listas de PedidoProdutoV2
    List<PedidoProdutoV2Dto> toPedidoProdutoV2DtoList(List<PedidoProdutoV2> pedidoProdutoList);
    List<PedidoProdutoV2> toPedidoProdutoV2EntityList(List<PedidoProdutoV2Dto> pedidoProdutoDtoList);
}