package gp6.harbor.harborapi.domain.pedido.repository;

import gp6.harbor.harborapi.domain.pedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Query("SELECT p FROM Pedido p JOIN p.prestador u WHERE u.nome LIKE CONCAT('%', :prestador, '%') ORDER BY u.nome")
    List<Pedido> listarPorPrestador(String prestador);
}
