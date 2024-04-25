package gp6.harbor.harborapi.domain.pedido;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DateTimeException;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime horaPedido;
    private int fkEmpresa;
    private int fkCliente;
}
