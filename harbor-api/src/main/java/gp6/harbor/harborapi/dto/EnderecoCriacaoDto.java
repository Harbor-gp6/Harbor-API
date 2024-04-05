package gp6.harbor.harborapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EnderecoCriacaoDto {

    private String bairro;
    @NotBlank(message = "O logradouro não pode estar em branco")
    private String logradouro;
    @NotBlank(message = "O cidade não pode estar em branco")
    private String cidade;
    @NotBlank(message = "O estado não pode estar em branco")
    private String estado;
    private String numero;
    @NotBlank(message = "O cep não pode estar em branco")
    @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 dígitos numéricos")
    private String cep;

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
