package com.harbor.projectharborapi.pedido;

import com.harbor.projectharborapi.cliente.Cliente;
import com.harbor.projectharborapi.prestadores.abstrato.PrestadorDeServico;
import com.harbor.projectharborapi.servicos.Servico;

import java.time.LocalDateTime;

public class Pedido {
    private int id;
    private LocalDateTime dataHora;
    private Servico servico;
    private Cliente cliente;
    private PrestadorDeServico prestadorDeServico;

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

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
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
