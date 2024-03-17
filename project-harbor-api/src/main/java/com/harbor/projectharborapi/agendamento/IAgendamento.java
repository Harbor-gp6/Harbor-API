package com.harbor.projectharborapi.agendamento;

import com.harbor.projectharborapi.cliente.Cliente;
import com.harbor.projectharborapi.pedido.Pedido;
import com.harbor.projectharborapi.prestadores.abstrato.PrestadorDeServico;
import com.harbor.projectharborapi.servicos.Servico;

import java.time.LocalDateTime;
import java.util.List;

public interface IAgendamento {
    Pedido agendarPedido(PrestadorDeServico p, Cliente c, LocalDateTime horario, List<Servico> servicos);
}
