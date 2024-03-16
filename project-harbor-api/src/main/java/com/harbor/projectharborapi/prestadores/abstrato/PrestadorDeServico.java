package com.harbor.projectharborapi.prestadores.abstrato;

import com.harbor.projectharborapi.agendamento.IAgendamento;
import com.harbor.projectharborapi.cliente.Cliente;
import com.harbor.projectharborapi.pedido.Pedido;
import com.harbor.projectharborapi.servicos.Servico;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class PrestadorDeServico implements IAgendamento {
    private int id;
    protected String nome;
    protected String sobrenome;
    protected String cpf;
    protected List<LocalTime> horariosDisponiveis = new ArrayList<>();
    protected int qtdServicosRealizados;
    protected boolean disponivel;
    protected List<Pedido> servicosAgendados = new ArrayList<>();

    @Override
    public Pedido agendarServico(PrestadorDeServico p, Cliente c, LocalDateTime horario, List<Servico> servico) {
        return null;
    }

    public void adicionarServico(Servico s) {

    }

    public void checkInServico(Servico servico) {

    }

    public void cancelarServico(Servico servico) {

    }

    public void marcarServicoComoConcluido(Servico servico) {
    }

    public int getId() {
        return id;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public List<LocalTime> getHorariosDisponiveis() {
        return horariosDisponiveis;
    }

    public int getQtdServicosRealizados() {
        return qtdServicosRealizados;
    }

    public void setQtdServicosRealizados(int qtdServicosRealizados) {
        this.qtdServicosRealizados = qtdServicosRealizados;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public List<Pedido> getServicosAgendados() {
        return servicosAgendados;
    }
}
