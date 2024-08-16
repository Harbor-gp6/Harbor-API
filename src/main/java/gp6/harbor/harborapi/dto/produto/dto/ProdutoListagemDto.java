package gp6.harbor.harborapi.dto.produto.dto;

import lombok.Data;

@Data
public class ProdutoListagemDto {
    private Integer id;
    private String nome;
    private String descricao;
    private Double precoCompra;
    private Double precoVenda;
}
