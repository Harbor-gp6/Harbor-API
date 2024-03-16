package com.harbor.projectharborapi.servicos;

import java.time.LocalDateTime;

public class Servico {
    private int id;
    private boolean iniciado;
    private boolean finalizado;
    private boolean cancelado;
    private String descricaoServico;
    private double valor;
    private int tempoMedioEmMinutos;
    private LocalDateTime dataHoraInicio;

    public int getId() {
        return id;
    }

    public boolean isIniciado() {
        return iniciado;
    }

    public void setIniciado(boolean iniciado) {
        this.iniciado = iniciado;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
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

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }
}
