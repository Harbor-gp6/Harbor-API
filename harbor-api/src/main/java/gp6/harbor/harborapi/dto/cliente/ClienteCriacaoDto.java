package gp6.harbor.harborapi.dto.cliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public class ClienteCriacaoDto {

    @NotBlank(message = "O nome não pode estar em branco")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;
    @NotBlank(message = "O sobrenome não pode estar em branco")
    @Size(min = 2, max = 100, message = "O sobrenome deve ter entre 2 e 100 caracteres")
    private String sobrenome;
    @NotBlank
    @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter apenas números e deve ter entre 10 ou 11 dígitos")
    private String telefone;
    @CPF
    @NotBlank
    private String cpf;

    private LocalDate dataCriacao;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
