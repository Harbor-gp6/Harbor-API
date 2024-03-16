package com.harbor.projectharborapi.prestadores;

import com.harbor.projectharborapi.prestadores.abstrato.PrestadorDeServico;

public class Funcionario extends PrestadorDeServico {
    private double valorHora;
    private int horasTrabalhadas;
    private double comissao;

    public double calcularSalario() {
        return (valorHora * horasTrabalhadas) + comissao;
    }

    public double getValorHora() {
        return valorHora;
    }

    public void setValorHora(double valorHora) {
        this.valorHora = valorHora;
    }

    public double getComissao() {
        return comissao;
    }
}
