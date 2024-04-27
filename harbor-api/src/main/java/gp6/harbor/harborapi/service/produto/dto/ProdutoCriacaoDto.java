package gp6.harbor.harborapi.service.produto.dto;

import gp6.harbor.harborapi.service.empresa.dto.EmpresaCriacaoDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProdutoCriacaoDto {
    @NotBlank(message = "O nome do produto não pode estar em branco")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;
    private String descricao;
    @NotNull
    private Double precoCompra;
    @NotNull
    private Double precoVenda;
    @Valid
    private EmpresaCriacaoDto empresa;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(Double precoCompra) {
        this.precoCompra = precoCompra;
    }

    public Double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(Double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public EmpresaCriacaoDto getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaCriacaoDto empresa) {
        this.empresa = empresa;
    }
}
