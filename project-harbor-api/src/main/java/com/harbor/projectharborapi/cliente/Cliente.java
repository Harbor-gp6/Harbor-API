package com.harbor.projectharborapi.cliente;

import com.harbor.projectharborapi.agendamento.IAgendamento;
import com.harbor.projectharborapi.prestadores.abstrato.PrestadorDeServico;
import com.harbor.projectharborapi.servicos.Servico;

import java.util.Date;
import java.util.Optional;

public class Cliente implements IAgendamento {
    private String nome;
    private String sobrenome;
    private String telefone;
    private String cpf;

    @Override
    public Servico agendarServico(PrestadorDeServico p, Date data, Servico servico) {
        return null;
    }

    @Override
    public Servico agendarServico(Cliente c, Date data, Servico servico) {
        return null;
    }

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
