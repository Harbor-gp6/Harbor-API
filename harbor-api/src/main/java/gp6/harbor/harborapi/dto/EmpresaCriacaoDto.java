package gp6.harbor.harborapi.dto;

import gp6.harbor.harborapi.entity.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.LocalDate;

public class EmpresaCriacaoDto {

    @NotBlank(message = "A razão social não pode estar em branco")
    @Size(min = 2, max = 300, message = "A razão social deve ter entre 2 e 300 caracteres")
    private String razaoSocial;

    @Size(max = 255)
    private String nomeFantasia;

    @CNPJ
    private String cnpj;

    @NotBlank
//    private Endereco endereco;
    private String endereco;

    private LocalDate dataCriacao;




    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }


    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
