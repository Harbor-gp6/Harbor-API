package gp6.harbor.harborapi.domain.pedido.repository;

import gp6.harbor.harborapi.domain.pedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Query("SELECT p FROM Pedido p JOIN p.prestador u WHERE u.nome LIKE CONCAT('%', :prestador, '%') ORDER BY u.nome")
    List<Pedido> listarPorPrestador(String prestador);

    List<Pedido> findByPrestadorId(Long prestadorId);

    @Query("SELECT SUM(p.total) from Pedido p WHERE p.dataAgendamento >= :dataInicio AND p.dataAgendamento <= :dataFim")
    Double somarFaturamentoBruto(LocalDateTime dataInicio, LocalDateTime dataFim);

    @Query("SELECT SUM(p.total) from Pedido p WHERE p.dataAgendamento >= :dataInicio AND p.dataAgendamento <= :dataFim AND p.prestador.id = :prestadorId")
    Double somarFaturamentoBrutoPorPrestador(LocalDateTime dataInicio, LocalDateTime dataFim, Long prestadorId);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.dataAgendamento >= :dataInicio AND p.dataAgendamento <= :dataFim")
    Integer contarPedidosPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim);

    List<Pedido> findAllByPrestadorId(Long prestadorId);
}
