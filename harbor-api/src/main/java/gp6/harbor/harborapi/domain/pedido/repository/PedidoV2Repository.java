package gp6.harbor.harborapi.domain.pedido.repository;

import gp6.harbor.harborapi.api.enums.StatusPedidoEnum;
import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.pedido.PedidoV2;
import org.springframework.data.jpa.repository.JpaRepository;

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



}
