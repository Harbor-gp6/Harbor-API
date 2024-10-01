package gp6.harbor.harborapi.domain.AtividadePedido.repository;

import gp6.harbor.harborapi.domain.AtividadePedido.AtividadePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AtividadePedidoRepository extends JpaRepository<AtividadePedido, UUID> {
    AtividadePedido findByCodigoPedido(UUID codigoPedido);
    List<AtividadePedido> findByCnpjEmpresa(String cnpjEmpresa);

    AtividadePedido findByCpfsContaining(String cpfPrestador);

}
