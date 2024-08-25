package gp6.harbor.harborapi.dto.pedido.dto;

import gp6.harbor.harborapi.api.enums.FormaPagamentoEnum;
import gp6.harbor.harborapi.domain.pedido.PedidoPrestador;
import gp6.harbor.harborapi.domain.pedido.PedidoProdutoV2;
import gp6.harbor.harborapi.dto.cliente.dto.ClienteCriacaoDto;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PedidoV2CriacaoDto {
    @NotNull
    private ClienteCriacaoDto cliente;

    @CNPJ
    @NotNull
    private String cnpjEmpresa;

    @NotNull
    private List<PedidoPrestadorDto> pedidoPrestador;

    private List<PedidoProdutoV2Dto> pedidoProdutos;

    //data de agendamento
    @Future
    private LocalDateTime dataAgendamento;

    @Enumerated(EnumType.STRING)
    private FormaPagamentoEnum formaPagamentoEnum;
}
