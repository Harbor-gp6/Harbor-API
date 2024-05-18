package gp6.harbor.harborapi.service.pedido;

import gp6.harbor.harborapi.domain.pedido.Pedido;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoRepository;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import gp6.harbor.harborapi.exception.RequisicaoInvalidaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PrestadorRepository prestadorRepository;

    public Pedido criarPedido(Pedido novoPedido) {
        Optional<Prestador> prestOpt = prestadorRepository.findById(novoPedido.getPrestador().getId());

        if (prestOpt.isEmpty()) {
            throw new RequisicaoInvalidaException("Prestador de serviço não existe");
        }

        return pedidoRepository.save(novoPedido);
    }
}
