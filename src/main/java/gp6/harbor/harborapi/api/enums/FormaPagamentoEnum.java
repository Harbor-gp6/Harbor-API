package gp6.harbor.harborapi.api.enums;


import lombok.Getter;

@Getter
public enum FormaPagamentoEnum {
    CREDITO(1),
    DEBITO(2),
    DINHEIRO(3),
    PIX(4);

    private int formaPagamento;

    FormaPagamentoEnum(int formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public static FormaPagamentoEnum fromCodigo(int codigo) {
        for (FormaPagamentoEnum forma : FormaPagamentoEnum.values()) {
            if (forma.formaPagamento == codigo) {
                return forma;
            }
        }
        throw new IllegalArgumentException("Código de forma de pagamento inválido: " + codigo);
    }


}
