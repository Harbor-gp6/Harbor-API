package gp6.harbor.harborapi.service.pedido;

import gp6.harbor.harborapi.domain.pedido.Pedido;
import gp6.harbor.harborapi.domain.pedido.PedidoServico;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoServicoRepository;
import gp6.harbor.harborapi.domain.servico.Servico;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoServicoService {

    private final PedidoServicoRepository pedidoServicoRepository;

    public PedidoServico adicionarServico(Servico servico, Pedido pedido) {
        PedidoServico pedidoServico = new PedidoServico();

        pedidoServico.setServico(servico);
        pedidoServico.setPedido(pedido);

        return pedidoServicoRepository.save(pedidoServico);
    }
}
