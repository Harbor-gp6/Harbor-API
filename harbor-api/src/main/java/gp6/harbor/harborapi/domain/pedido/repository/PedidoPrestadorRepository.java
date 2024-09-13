package gp6.harbor.harborapi.domain.pedido.repository;

import gp6.harbor.harborapi.domain.pedido.PedidoPrestador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoPrestadorRepository extends JpaRepository<PedidoPrestador, Integer> {
    ////encontre todos os pedidos finalizados buscando por Idprestador
    //    List<Pedido> findAllByPrestadorIdAndStatus(Long prestadorId, StatusPedidoEnum statusPedidoEnum);
    List<PedidoPrestador> findAllByPrestadorId(Long prestadorId);

    //Double somarFaturamentoBrutoPorPrestador(LocalDateTime dataInicio, LocalDateTime dataFim, Long prestadorId);
    //quero que some o total de todos os pedidos de um prestador
}