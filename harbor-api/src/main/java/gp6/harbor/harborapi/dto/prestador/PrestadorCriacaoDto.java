package gp6.harbor.harborapi.dto.prestador;

import gp6.harbor.harborapi.dto.empresa.EmpresaCriacaoDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public class PrestadorCriacaoDto {
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
    @NotBlank(message = "O email não pode estar em branco")
    private String cpf;
    @Email
    @NotBlank(message = "O email não pode estar em branco")
    private String email;
    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 8, max = 30)
    private String senha;
    @NotBlank
    private String cargo;
    @Valid
    private EmpresaCriacaoDto empresa;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public EmpresaCriacaoDto getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaCriacaoDto empresa) {
        this.empresa = empresa;
    }
}
