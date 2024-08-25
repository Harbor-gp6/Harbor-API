package gp6.harbor.harborapi.domain.pedido.repository;

import gp6.harbor.harborapi.domain.empresa.Empresa;
import gp6.harbor.harborapi.domain.pedido.PedidoV2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoV2Repository extends JpaRepository<PedidoV2, Integer> {
    List<PedidoV2> findByEmpresa(Empresa empresa);
    List<PedidoV2> findByEmpresaAndFinalizado(Empresa empresa , boolean finalizado);

    List<PedidoV2> findByPedidoPrestadorPrestadorCpf(String cpf);

    //listar pedidosV2 abertos somente daquele prestador
    List<PedidoV2> findByPedidoPrestadorPrestadorCpfAndFinalizado(String cpf, boolean finalizado);

}
