package gp6.harbor.harborapi.service.empresa.dto;

import gp6.harbor.harborapi.service.endereco.dto.EnderecoCriacaoDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.LocalDate;

@Getter
@Setter
public class EmpresaCriacaoDto {

    @NotBlank(message = "A razão social não pode estar em branco")
    @Size(min = 2, max = 300, message = "A razão social deve ter entre 2 e 300 caracteres")
    private String razaoSocial;

    @Size(max = 255)
    private String nomeFantasia;

    @CNPJ
    private String cnpj;

//    private Endereco endereco;
    @Valid
    private EnderecoCriacaoDto endereco;

    private LocalDate dataCriacao;
    private LocalDate dataInativacao;





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


    public EnderecoCriacaoDto getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoCriacaoDto endereco) {
        this.endereco = endereco;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataInativacao() {
        return dataInativacao;
    }

    public void setDataInativacao(LocalDate dataInativacao) {
        this.dataInativacao = dataInativacao;
    }
}