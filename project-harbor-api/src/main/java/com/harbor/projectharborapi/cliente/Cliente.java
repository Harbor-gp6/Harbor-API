package com.harbor.projectharborapi.cliente;

import com.harbor.projectharborapi.agendamento.IAgendamento;
import com.harbor.projectharborapi.pedido.Pedido;
import com.harbor.projectharborapi.prestadores.abstrato.PrestadorDeServico;


public class Cliente implements IAgendamento {
    private String nome;
    private String sobrenome;
    private String telefone;
    private String cpf;

    @Override
    public Pedido agendarPedido(PrestadorDeServico p, Cliente c, Pedido novoPedido) {
        if (c.nome == null || c.sobrenome == null || c.cpf == null || c.telefone == null) {
            return null;
        }

        novoPedido.setCliente(c);
        novoPedido.setPrestadorDeServico(p);
        p.adicionarPedido(novoPedido);

        return novoPedido;
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
