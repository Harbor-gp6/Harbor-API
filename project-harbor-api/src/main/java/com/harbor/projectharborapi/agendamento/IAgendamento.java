package com.harbor.projectharborapi.agendamento;

import com.harbor.projectharborapi.cliente.Cliente;
import com.harbor.projectharborapi.prestadores.abstrato.PrestadorDeServico;
import com.harbor.projectharborapi.servicos.Servico;

import java.util.Date;
import java.util.Optional;

public interface IAgendamento {
    Servico agendarServico(Cliente c, Date data, Servico servico);
    Servico agendarServico(PrestadorDeServico p, Date data, Servico servico);
}
