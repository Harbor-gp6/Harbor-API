package com.harbor.projectharborapi.agendamento;

import com.harbor.projectharborapi.cliente.Cliente;
import com.harbor.projectharborapi.pedido.Pedido;
import com.harbor.projectharborapi.prestadores.abstrato.PrestadorDeServico;


public interface IAgendamento {
    Pedido agendarPedido(PrestadorDeServico p, Cliente c, Pedido novoPedido);
}
