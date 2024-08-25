package gp6.harbor.harborapi.dto.produto.dto;

import gp6.harbor.harborapi.dto.empresa.dto.EmpresaCriacaoDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProdutoCriacaoDto {
    @NotBlank(message = "O nome do produto não pode estar em branco")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;
    private String descricao;
    @NotNull
    private Double precoCompra;
    @NotNull
    private Double precoVenda;
}
