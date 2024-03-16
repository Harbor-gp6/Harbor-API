package com.harbor.projectharborapi.servicos;

public class Servico {
    private int id;
    private String descricaoServico;
    private double valor;
    private int tempoMedioEmMinutos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricaoServico() {
        return descricaoServico;
    }

    public void setDescricaoServico(String descricaoServico) {
        this.descricaoServico = descricaoServico;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getTempoMedioEmMinutos() {
        return tempoMedioEmMinutos;
    }

    public void setTempoMedioEmMinutos(int tempoMedioEmMinutos) {
        this.tempoMedioEmMinutos = tempoMedioEmMinutos;
    }
}
