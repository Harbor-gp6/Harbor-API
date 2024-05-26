package gp6.harbor.harborapi.dto.pedido.dto;

import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.dto.cliente.dto.ClienteCriacaoDto;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PedidoCriacaoDto {

    @NotNull
    private ClienteCriacaoDto cliente;

    @NotNull
    @Future
    private LocalDateTime dataAgendamento;

    @NotNull
    private List<Integer> servicos;

    @NotNull
    private Long prestadorId;

    @NotNull
    private Integer formaPagamento;
}