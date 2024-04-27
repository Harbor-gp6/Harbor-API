package gp6.harbor.harborapi.domain.pedido.repository;

import gp6.harbor.harborapi.domain.pedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

}
