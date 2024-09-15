package gp6.harbor.harborapi.dto.pedido.dto;

import gp6.harbor.harborapi.domain.pedido.PedidoPrestador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoPrestadorMapper {
    @Mapping(source = "prestador.id", target = "prestadorId")
    @Mapping(source = "servico.id", target = "servicoId")
    PedidoPrestadorDto toDto(PedidoPrestador pedidoPrestador);

    List<PedidoPrestadorDto> toDto(List<PedidoPrestador> pedidoPrestadorList);
}