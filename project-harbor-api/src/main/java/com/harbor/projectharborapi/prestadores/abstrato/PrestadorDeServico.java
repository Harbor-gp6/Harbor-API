package com.harbor.projectharborapi.prestadores.abstrato;

import com.harbor.projectharborapi.agendamento.IAgendamento;
import com.harbor.projectharborapi.cliente.Cliente;
import com.harbor.projectharborapi.pedido.Pedido;
import com.harbor.projectharborapi.servicos.Servico;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public abstract class PrestadorDeServico implements IAgendamento {
    private int id;
    protected String nome;
    protected String sobrenome;
    protected String cpf;
    protected List<LocalTime> horarios = new ArrayList<>();
    protected List<LocalDateTime> horariosDisponiveis = new ArrayList<>();
    protected int qtdServicosRealizados;
    protected boolean disponivel;
    protected List<Pedido> servicosAgendados = new ArrayList<>();

    @Override
    public Pedido agendarPedido(PrestadorDeServico p, Cliente c, Pedido novoPedido) {
        if (c.getNome() == null || c.getSobrenome() == null) {
            return null;
        }

        novoPedido.setPrestadorDeServico(p);
        novoPedido.setCliente(c);
        p.adicionarPedido(novoPedido);

        return novoPedido;
    }

    public Pedido adicionarPedido(Pedido p) {
        if (horariosDisponiveis.stream().anyMatch(h -> h.equals(p.getDataHora()))) {
            return null;
        }

        horariosDisponiveis.remove(p.getDataHora());
        servicosAgendados.add(p);
        return p;
    }

    public void checkInPedido(Pedido pedido) {
        pedido.setStatus("Iniciado");
    }

    public void cancelarPedido(Pedido pedido) {
        pedido.setStatus("Cancelado");
        horariosDisponiveis.add(pedido.getDataHora());
    }

    public void marcarServicoComoConcluido(Pedido pedido) {
    }

    public void adicionarHorario(LocalTime horario) {
        horarios.add(horario);
    }

    public void setId(int id) {
        this.id = id;
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

    public List<LocalDateTime> getHorariosDisponiveis() {
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
