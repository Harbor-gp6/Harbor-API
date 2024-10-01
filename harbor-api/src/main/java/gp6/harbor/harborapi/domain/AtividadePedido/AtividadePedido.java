package gp6.harbor.harborapi.domain.AtividadePedido;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gp6.harbor.harborapi.api.enums.StatusPedidoEnum;
import gp6.harbor.harborapi.domain.prestador.AvaliacaoPrestador;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class AtividadePedido {
    @Id
    private UUID id = UUID.randomUUID();
    @Enumerated(EnumType.STRING)
    private StatusPedidoEnum statusPedidoEnum;
    @DateTimeFormat
    private LocalDateTime dataCriacao = LocalDateTime.now();
    private UUID codigoPedido;
    private String cnpjEmpresa;
    @ElementCollection
    private List<String> cpfs = new ArrayList<>();

}
