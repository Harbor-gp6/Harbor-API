package gp6.harbor.harborapi.dto.pedido.dto;

import gp6.harbor.harborapi.domain.pedido.PedidoProdutoV2;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoProdutoV2Mapper {
    PedidoProdutoV2Dto toDto(PedidoProdutoV2 pedidoProdutoV2);
    List<PedidoProdutoV2Dto> toDto(List<PedidoProdutoV2> pedidoProdutoV2List);
}