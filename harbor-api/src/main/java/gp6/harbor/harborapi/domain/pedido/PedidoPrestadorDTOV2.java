package gp6.harbor.harborapi.domain.pedido;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PedidoPrestadorDTOV2 {
    private Integer idPrestador;
    private Integer idServico;
    private String nomePrestador;
    private String foto;
    private String descricaoServico;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Double valorServico;
}
