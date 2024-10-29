package gp6.harbor.harborapi.domain.pedido.repository;

import gp6.harbor.harborapi.api.enums.StatusPedidoEnum;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.pedido.PedidoV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PedidoV2Repository extends JpaRepository<PedidoV2, Integer> {
    List<PedidoV2> findByEmpresa(Empresa empresa);
    List<PedidoV2> findByEmpresaAndStatusPedidoEnum(Empresa empresa , StatusPedidoEnum statusPedidoEnum);
    List<PedidoV2> findByPedidoPrestadorPrestadorCpf(String cpf);

    //listar pedido por data e por cpf do prestador
    List<PedidoV2> findByDataAgendamentoBetweenAndPedidoPrestadorPrestadorCpf(LocalDateTime dataInicio, LocalDateTime dataFim, String cpf);
    List<PedidoV2> findByPedidoPrestadorPrestadorCpfAndStatusPedidoEnum(String cpf, StatusPedidoEnum statusPedidoEnum);
    PedidoV2 findByCodigoPedido(UUID codigoPedido);
    @Query("SELECT SUM(p.totalPedido) from PedidoV2 p WHERE p.dataAgendamento >= :dataInicio AND p.dataAgendamento <= :dataFim AND p.empresa.id = :empresaId AND p.statusPedidoEnum = 'FINALIZADO'")
    Double somarFaturamentoBrutoPorEmpresa(LocalDateTime dataInicio, LocalDateTime dataFim, Integer empresaId);
    @Query("SELECT p.formaPagamentoEnum, COUNT(p) FROM PedidoV2 p WHERE p.dataAgendamento >= :dataInicio AND p.dataAgendamento <= :dataFim AND p.empresa.id = :empresaId AND p.statusPedidoEnum = 'FINALIZADO' GROUP BY p.formaPagamentoEnum")
    List<Object[]> countByDataAgendamentoBetweenAndEmpresaIdAndFormaPagamentoEnum(LocalDateTime dataInicio, LocalDateTime dataFim, Integer empresaId);

}
