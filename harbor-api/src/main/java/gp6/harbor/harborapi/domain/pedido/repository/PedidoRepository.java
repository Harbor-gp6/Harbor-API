package gp6.harbor.harborapi.domain.pedido.repository;

import gp6.harbor.harborapi.domain.pedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Query("SELECT Pedido FROM Pedido p JOIN Prestador u ON p.id_prestador = u.id WHERE u.nome LIKE '%:prestador%'")
    List<Pedido> listarPorPrestador(String prestador);
}
