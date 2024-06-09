package gp6.harbor.harborapi.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.pedido.Pedido;

public class PedidoFilaEspera <T extends Pedido> {

    private final Map<Cliente, Queue<T>> filasPorCliente;
    private final int capacidadeMaxima;

    public PedidoFilaEspera(int capacidadeMaxima) {
        this.filasPorCliente = new HashMap<>();
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public boolean adicionarPedido(T pedido) {

        Cliente cliente = pedido.getCliente();

        Queue<T> filaCliente = filasPorCliente.get(cliente);

        if(filaCliente == null) {
            filaCliente = new LinkedList<>();
            filasPorCliente.put(cliente, filaCliente);
        }

        if(filaCliente.size() >= capacidadeMaxima) {
            return false;
        }

        filaCliente.add((T) pedido);
        return true;

    }

    public T prestarProximoPedido(Cliente cliente) {

        Queue<T> filaCliente = filasPorCliente.get(cliente);

        if(filaCliente.isEmpty()) {
            return null;
        }

        return filaCliente.poll();

    }

    public int tamanhoFila() {
        return capacidadeMaxima;
    }

    public boolean filaVazia() {
        return capacidadeMaxima == 0;
    }

    public boolean filaCheia() {
        return filasPorCliente.size() == capacidadeMaxima;
    }
}
