package gp6.harbor.harborapi.api.enums;


import lombok.Getter;

@Getter
public enum FormaPagamento {
    CREDITO(1),
    DEBITO(2),
    DINHEIRO(3),
    PIX(4);

    private int formaPagamento;

    FormaPagamento(int formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public static FormaPagamento fromCodigo(int codigo) {
        for (FormaPagamento forma : FormaPagamento.values()) {
            if (forma.formaPagamento == codigo) {
                return forma;
            }
        }
        throw new IllegalArgumentException("Código de forma de pagamento inválido: " + codigo);
    }


}
