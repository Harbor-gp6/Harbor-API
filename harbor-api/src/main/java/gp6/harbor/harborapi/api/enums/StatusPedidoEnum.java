package gp6.harbor.harborapi.api.enums;

import lombok.Getter;

@Getter
public enum StatusPedidoEnum {
    ABERTO("Aberto"),
    FINALIZADO("Finalizado"),
    CANCELADO("Cancelado");

    private String status;

    StatusPedidoEnum(String status) {
        this.status = status;
    }
}