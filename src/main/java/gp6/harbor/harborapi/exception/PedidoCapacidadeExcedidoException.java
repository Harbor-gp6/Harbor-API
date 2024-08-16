package gp6.harbor.harborapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class PedidoCapacidadeExcedidoException extends RuntimeException{

    public PedidoCapacidadeExcedidoException(String mensagem) {
        super(mensagem);
    }
}
