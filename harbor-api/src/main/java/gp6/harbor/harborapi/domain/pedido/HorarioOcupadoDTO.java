package gp6.harbor.harborapi.domain.pedido;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class HorarioOcupadoDTO {

    private Long pedidoId;
    private String nomeServico;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
}
