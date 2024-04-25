package gp6.harbor.harborapi.service.produto.dto;
import gp6.harbor.harborapi.domain.produto.Produto;

import java.util.List;

public class ProdutoMapper {
    public static Produto toEntity(ProdutoCriacaoDto dto){
        if (dto == null){
            return null;
        }
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPrecoCompra(dto.getPrecoCompra());
        produto.setPrecoVenda(dto.getPrecoVenda());

        return produto;
    }
    public static ProdutoListagemDto toDto(Produto entity){
        if (entity == null) return null;

        ProdutoListagemDto listagemDto = new ProdutoListagemDto();
        listagemDto.setId(entity.getId());
        listagemDto.setNome(entity.getNome());
        listagemDto.setDescricao(entity.getDescricao());
        listagemDto.setPrecoCompra(entity.getPrecoCompra());
        listagemDto.setPrecoVenda(entity.getPrecoVenda());

        return listagemDto;
    }
    public static List<ProdutoListagemDto> toDto(List<Produto> entities){
        return entities.stream().map(ProdutoMapper::toDto).toList();
    }
}
