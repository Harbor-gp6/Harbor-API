package gp6.harbor.harborapi.dto.prestador.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AvaliacaoPrestadorDto {
    private UUID codigoPedido;
    private Integer idCliente;
    private List<PrestadorEstrelasDto> avaliacoes;
}
