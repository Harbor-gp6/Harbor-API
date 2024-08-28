package gp6.harbor.harborapi.service.prestador;

import gp6.harbor.harborapi.api.enums.StatusPedidoEnum;
import gp6.harbor.harborapi.domain.cliente.Cliente;
import gp6.harbor.harborapi.domain.cliente.repository.ClienteRepository;
import gp6.harbor.harborapi.domain.pedido.PedidoV2;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoRepository;
import gp6.harbor.harborapi.domain.pedido.repository.PedidoV2Repository;
import gp6.harbor.harborapi.domain.prestador.AvaliacaoPrestador;
import gp6.harbor.harborapi.domain.prestador.Prestador;
import gp6.harbor.harborapi.domain.prestador.repository.AvaliacaoRepository;
import gp6.harbor.harborapi.domain.prestador.repository.PrestadorRepository;
import gp6.harbor.harborapi.service.cliente.ClienteService;
import gp6.harbor.harborapi.service.pedido.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvaliacaoPrestadorService {
    private final AvaliacaoRepository avaliacaoRepository;
    private final PrestadorService prestadorService;
    private final PedidoService pedidoService;
    private final PedidoV2Repository pedidoV2Repository;
    private final ClienteService clienteService;
    private final PrestadorRepository prestadorRepository;

    //criar avaliação de prestador caso o pedido já esteja finalizado
    public void criarAvaliacaoPrestador(UUID codigoPedido, Long idPrestador, Double estrelas, String comentario, Integer idCliente) {
        Cliente cliente = clienteService.buscarPorId(idCliente);
        PedidoV2 pedido = pedidoV2Repository.findByCodigoPedido(codigoPedido).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
        Prestador prestador = prestadorService.buscarPorId(idPrestador);

        if (!(pedido.getCliente().getId().equals(cliente.getId())) || pedido.getStatusPedidoEnum() != StatusPedidoEnum.FINALIZADO || pedido.getPedidoPrestador().stream().noneMatch(p -> p.getPrestador().getId().equals(prestador.getId()))) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Pedido não finalizado ou não pertence ao cliente ou prestador");
        }

        for (AvaliacaoPrestador avaliacaoPrestador : prestador.getAvaliacoes()) {
            if (avaliacaoPrestador.getCodigoPedido().equals(pedido.getCodigoPedido())) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Prestador já avaliado");
            }
        }
        AvaliacaoPrestador avaliacaoPrestador = prestadorService.criarAvaliacaoPrestador(pedido, prestador, estrelas, comentario, cliente);
        prestador.getAvaliacoes().add(avaliacaoPrestador);
        prestador.atualizarEstrelas(prestador, estrelas);

        prestadorRepository.save(prestador);
    }
}
