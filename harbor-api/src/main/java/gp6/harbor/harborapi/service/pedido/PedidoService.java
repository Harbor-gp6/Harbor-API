package gp6.harbor.harborapi.service.pedido;

import gp6.harbor.harborapi.domain.pedido.Pedido;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoRepository;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.domain.servico.Servico;
import gp6.harbor.harborapi.dto.pedido.dto.PedidoMapper;
import gp6.harbor.harborapi.exception.NaoEncontradoException;
import gp6.harbor.harborapi.exception.RequisicaoInvalidaException;
import gp6.harbor.harborapi.service.prestador.PrestadorService;
import gp6.harbor.harborapi.service.servico.ServicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PrestadorService prestadorService;
    private final PedidoProdutoService pedidoProdutoService;
    private final PedidoServicoService pedidoServicoService;
    private final ServicoService servicoService;

    public Pedido criarPedido(Pedido novoPedido, List<Integer> servicosIds) {
        Prestador prestador = prestadorService.buscarPorId(novoPedido.getPrestador().getId());

        novoPedido.setPrestador(prestador);

        Pedido pedidoSalvo = pedidoRepository.save(novoPedido);

        servicosIds.forEach((id) -> {
            Servico servico = servicoService.buscaPorId(id);
            pedidoServicoService.adicionarServico(servico, pedidoSalvo);
        });

        return pedidoSalvo;
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPorPrestador(String nomePrestador) {
        return pedidoRepository.listarPorPrestador(nomePrestador);
    }
}
