package com.harbor.projectharborapi.barbearia;

import com.harbor.projectharborapi.prestadores.abstrato.PrestadorDeServico;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Barbearia {
    private int id;
    private String cnpj;
    private String nome;
    private LocalTime horarioAbertura;
    private LocalTime horarioFechamento;
    private double lucroTotal;
    private List<PrestadorDeServico> prestadoresDeServico = new ArrayList<>();

    public void adicionarPrestadorDeServico(PrestadorDeServico p) {
        prestadoresDeServico.add(p);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalTime getHorarioAbertura() {
        return horarioAbertura;
    }

    public void setHorarioAbertura(LocalTime horarioAbertura) {
        this.horarioAbertura = horarioAbertura;
    }

    public LocalTime getHorarioFechamento() {
        return horarioFechamento;
    }

    public void setHorarioFechamento(LocalTime horarioFechamento) {
        this.horarioFechamento = horarioFechamento;
    }

    public double getLucroTotal() {
        return lucroTotal;
    }

    public List<PrestadorDeServico> getPrestadoresDeServico() {
        return prestadoresDeServico;
    }
}
