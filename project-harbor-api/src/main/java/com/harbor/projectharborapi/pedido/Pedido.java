package com.harbor.projectharborapi.pedido;

import com.harbor.projectharborapi.cliente.Cliente;
import com.harbor.projectharborapi.prestadores.abstrato.PrestadorDeServico;
import com.harbor.projectharborapi.servicos.Servico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id = 0;
    private LocalDateTime dataHora;
    private List<Servico> servicos = new ArrayList<>();
    private String status;
    private Cliente cliente;
    private PrestadorDeServico prestadorDeServico;

    public Pedido() {
    }

    public Pedido(LocalDateTime dataHora, List<Servico> servico, Cliente cliente, PrestadorDeServico prestadorDeServico) {
        ++this.id;
        this.dataHora = dataHora;
        this.servicos = servico;
        this.cliente = cliente;
        this.status = "Não iniciado";
        this.prestadorDeServico = prestadorDeServico;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public List<Servico> getServico() {
        return servicos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public PrestadorDeServico getPrestadorDeServico() {
        return prestadorDeServico;
    }

    public void setPrestadorDeServico(PrestadorDeServico prestadorDeServico) {
        this.prestadorDeServico = prestadorDeServico;
    }
}
