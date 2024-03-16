package com.harbor.projectharborapi.prestadores;

import com.harbor.projectharborapi.barbearia.Barbearia;
import com.harbor.projectharborapi.prestadores.abstrato.PrestadorDeServico;

public class Proprietario extends PrestadorDeServico {
    private double proLabore;

    public double calcularDistribuicaoDeLucros(Barbearia b) {
        return b.getLucroTotal() - proLabore;
    }

    public double getProLabore() {
        return proLabore;
    }

    public void setProLabore(double proLabore) {
        this.proLabore = proLabore;
    }
}
