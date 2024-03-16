package com.harbor.projectharborapi.prestadores.abstrato;

import com.harbor.projectharborapi.agendamento.IAgendamento;
import com.harbor.projectharborapi.cliente.Cliente;
import com.harbor.projectharborapi.servicos.Servico;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public abstract class PrestadorDeServico implements IAgendamento {
    private int id;
    protected String nome;
    protected String sobrenome;
    protected String cpf;
    protected List<LocalTime> horariosDisponiveis;
    protected int qtdServicosRealizados;
    protected boolean disponivel;

    @Override
    public Servico agendarServico(Cliente c, Date data, Servico servico) {
        return null;
    }

    @Override
    public Servico agendarServico(PrestadorDeServico p, Date data, Servico servico) {
        return null;
    }

    public void checkInServico(Servico servico) {
        servico.setIniciado(true);
    }

    public void cancelarServico(Servico servico) {

    }

    public void marcarServicoComoConcluido(Servico servico) {
        servico.setFinalizado(true);
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
}
