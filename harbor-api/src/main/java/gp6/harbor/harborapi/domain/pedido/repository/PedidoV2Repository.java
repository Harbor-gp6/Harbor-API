package gp6.harbor.harborapi.domain.pedido.repository;

import gp6.harbor.harborapi.api.enums.StatusPedidoEnum;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.pedido.PedidoV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PedidoV2Repository extends JpaRepository<PedidoV2, Integer> {
    List<PedidoV2> findByEmpresa(Empresa empresa);
    List<PedidoV2> findByEmpresaAndStatusPedidoEnum(Empresa empresa , StatusPedidoEnum statusPedidoEnum);

    List<PedidoV2> findByPedidoPrestadorPrestadorCpf(String cpf);

    //listar pedidosV2 abertos somente daquele prestador
    List<PedidoV2> findByPedidoPrestadorPrestadorCpfAndStatusPedidoEnum(String cpf, StatusPedidoEnum statusPedidoEnum);
    Optional<PedidoV2> findByCodigoPedido(UUID codigoPedido);

// //somar faturamento bruto por empresa
//    @Query("SELECT SUM(p.total) from Pedido p WHERE p.dataAgendamento >= :dataInicio AND p.dataAgendamento <= :dataFim AND p.prestador.empresa.id = :empresaId")
//    Double somarFaturamentoBrutoPorEmpresa(LocalDateTime dataInicio, LocalDateTime dataFim, Integer empresaId);
    @Query("SELECT SUM(p.totalPedido) from PedidoV2 p WHERE p.dataAgendamento >= :dataInicio AND p.dataAgendamento <= :dataFim AND p.empresa.id = :empresaId")
    Double somarFaturamentoBrutoPorEmpresa(LocalDateTime dataInicio, LocalDateTime dataFim, Integer empresaId);

    //countByDataAgendamentoBetweenAndEmpresaIdAndFormaPagamentoEnum
    //quero que traga uma lista de forma de pagamento e a quantidade de pedidos que tem nessa forma de pagamento
    //Quero que conte somente os pedidos que o status seja finalizado StatusPedidoEnum.FINALIZADO
    @Query("SELECT p.formaPagamentoEnum, COUNT(p) FROM PedidoV2 p WHERE p.dataAgendamento >= :dataInicio AND p.dataAgendamento <= :dataFim AND p.empresa.id = :empresaId AND p.statusPedidoEnum = 'FINALIZADO' GROUP BY p.formaPagamentoEnum")
    List<Object[]> countByDataAgendamentoBetweenAndEmpresaIdAndFormaPagamentoEnum(LocalDateTime dataInicio, LocalDateTime dataFim, Integer empresaId);
}
