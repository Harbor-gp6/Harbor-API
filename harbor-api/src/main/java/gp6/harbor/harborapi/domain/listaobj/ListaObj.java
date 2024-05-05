package gp6.harbor.harborapi.domain.listaobj;

public class ListaObj <T> {

    private T[] vetor;

    private int nroElem;

    public ListaObj(int tamanho) {
        vetor = (T[]) new Object[tamanho];
    }

    public void adiciona(T elemento) {
        if (nroElem == vetor.length) {
            throw new IllegalStateException("Lista cheia!");
        } else {
            vetor[nroElem++] = elemento;
        }
    }

    public int busca(T elementoBuscado) {
        for (int i = 0; i < nroElem; i++) {
            if (vetor[i].equals(elementoBuscado)) {
                return i;
            }
        }
        return -1;
    }

    public boolean removePeloIndice(int indice) {
        if (indice >= nroElem || indice < 0) {
            return false;
        }

        for (int i = indice; i < nroElem -1; i++) {
            vetor[i] = vetor[i + 1];
        }
        nroElem--;
        return true;
    }

    public boolean removeElemento(T elementoARemover) {
        return removePeloIndice(busca(elementoARemover));
    }

    public int getTamanho() {
        return nroElem;
    }

    public T getElemento(int indice) {
        if (indice < 0) {
            return null;
        }
        return vetor[indice];
    }

    public void limpa() {
        for (int i = 0; i < nroElem; i++) {
            vetor[i] = null;
        }
        nroElem = 0;
    }

    public void exibe() {
        System.out.print("Lista: ");
        for (int i = 0; i < nroElem; i++) {
            System.out.print(vetor[i]);
            if (i < nroElem - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("\n");
    }
}

