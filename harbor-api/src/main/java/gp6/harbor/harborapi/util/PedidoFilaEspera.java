package gp6.harbor.harborapi.util;

public class PedidoFilaEspera <T> {
    // Atributos
    private T[] fila;
    private int tamanho;
    private int capacidadeMaxima;

    // Construtor
    public PedidoFilaEspera(int capacidadeMaxima) {
        this.fila = (T[]) new Object[capacidadeMaxima];
        this.tamanho = 0;
        this.capacidadeMaxima = capacidadeMaxima;
    }

    // Métodos
    public boolean adicionarPedidoNaFila(T elemento) {
        if (tamanho == capacidadeMaxima) {
            return false; // Fila cheia, não é possível enfileirar
        }

        fila[tamanho] = elemento;
        tamanho++;
        return true;
    }

    public T prestarProximoPedido() {
        if (tamanho == 0) {
            return null; // Fila vazia, não há elementos a desenfileirar
        }

        T elementoRemovido = fila[0];
        deslocarPedidoNaFila();
        tamanho--;
        return elementoRemovido;
    }

    private void deslocarPedidoNaFila() {
        for (int i = 0; i < tamanho - 1; i++) {
            fila[i] = fila[i + 1];
        }
    }

    public int tamanhoFila() {
        return tamanho;
    }

    public boolean filaVazia() {
        return tamanho == 0;
    }

    public boolean filaCheia() {
        return tamanho == capacidadeMaxima;
    }
}
